using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using MongoDB.Bson;
using MongoDB.Driver;
using smartpublicrestroom.Code;
using smartpublicrestroom.Models;

namespace smartpublicrestroom.Controllers
{
    [Route("api/manager")]
    [ApiController]
    public class ManagerController : ControllerBase
    {
        public class AddRestroomData
        {
            public string guid { get; set; }
            public string cityAddress { get; set; }
            public string address { get; set; }
            public string company { get; set; }
        }
        public class AddRestroomResult: BaseResult
        {
            public int restroomId { get; set; }
        }
        public class LoginData
        {
            public string username { get; set; }
            public string password { get; set; }
        }
        public class RegisterData : LoginData
        {
            public string fullname { get; set; }
        }
        public class LoginResult : BaseResult
        {
            public string loginToken { get; set; }
            public User user { get; set; }
        }
        public class GetRestroomsResult: BaseResult
        {
            public List<RestRoom> restrooms { get; set; }
        }

        public class ReportsResult : BaseResult
        {
            public List<Report> reports { get; set; }
        }

        private readonly IMongoDatabase _db;
        public ManagerController(IMongoDatabase db)
        {
            _db = db;
        }
        
        [Route("getRestrooms")]
        [HttpPost] 
        public ActionResult<GetRestroomsResult> GetToilets()
        {
            GetRestroomsResult result = new GetRestroomsResult();
            if (!ValidUserSession())
                return result;
            
            IMongoCollection<Models.RestRoom> restroomsCollection = _db.GetCollection<Models.RestRoom>("Restroom");
            result.restrooms = restroomsCollection.AsQueryable().ToList();
            
            return result;
        }

        [Route("addRestroom")]
        [HttpPost]
        public ActionResult<AddRestroomResult> AddRestroom(AddRestroomData addRestroomData)
        {
            AddRestroomResult result = new AddRestroomResult();
            if (!ValidUserSession())
                return result;
            
            IMongoCollection<Models.RestRoom> restroomsCollection = _db.GetCollection<Models.RestRoom>("Restroom");
            Models.RestRoom newRestroom = new Models.RestRoom(addRestroomData.guid, addRestroomData.address.Split(','), addRestroomData.cityAddress, addRestroomData.company, "");
            restroomsCollection.InsertOne(newRestroom);

            result.Result = true;
            return result;
        }

        [Route("login")]
        [HttpPost]
        public ActionResult<LoginResult> Login(LoginData loginData)
        {
            LoginResult result = new LoginResult();

            string loginToken = Request.Headers["loginToken"].FirstOrDefault();
            // username-password authentication
            if (loginToken == null)
            {
                IMongoCollection<User> usersCollection = _db.GetCollection<User>("User");
                User user = usersCollection.Find(currUser => loginData.username == currUser.username).FirstOrDefault();
                if (user == null || !PasswordHash.ValidatePassword(loginData.password, user.password))
                {
                    result.message = "username or password incorrect";
                    return result;
                }

                IMongoCollection<Login> loginCollection = _db.GetCollection<Login>("Login");
                Login login = new Login(ObjectId.GenerateNewId(), user, Guid.NewGuid().ToString());
                loginCollection.InsertOne(login);
                result.loginToken = login.Logintoken;
                result.user = user;
            }

            // token-based authentication
            if (loginToken != null)
            {
                IMongoCollection<Login> loginCollections = _db.GetCollection<Login>("Login");
                Login login = loginCollections.Find(currLogin => currLogin.Logintoken == loginToken).FirstOrDefault();
                if (login == null)
                {
                    result.message = "wrong loginToken";
                    return result;
                }
                login.Timestamp = DateTime.Now;
                loginCollections.ReplaceOne(currLogin => currLogin._id == login._id, login);
                result.user = login.user;
            }

            result.Result = true;
            return result;
        }

        [Route("register")]
        [HttpPost]
        public ActionResult<LoginResult> Register(RegisterData loginData)
        {
            LoginResult result = new LoginResult();

            IMongoCollection<User> usersCollection = _db.GetCollection<User>("User");
            if(usersCollection.Find(user => loginData.username == user.username).Any())
            {
                result.message = "username already in use";
                return result;
            }

            User newUser = new User(ObjectId.GenerateNewId(), loginData.username, PasswordHash.HashPassword(loginData.password), loginData.fullname);
            usersCollection.InsertOne(newUser);
            
            IMongoCollection<Login> loginCollection = _db.GetCollection<Login>("Login");
            loginCollection.InsertOne(new Login(ObjectId.GenerateNewId(), newUser, Guid.NewGuid().ToString()));

            result.Result = true;
            return result;
        }

        [Route("reports")]
        [HttpPost]
        public ActionResult<ReportsResult> GetReports(LoginData data)
        {
            ReportsResult result = new ReportsResult();
            if (!ValidUserSession())
                return result;

            IMongoCollection<Models.Report> reportsCollection = _db.GetCollection<Models.Report>("Report");
            result.reports = reportsCollection.AsQueryable().ToList();

            result.Result = true;
            return result;
        }

        public bool ValidUserSession()
        {
            string loginToken = Request.Headers["loginToken"].FirstOrDefault();
            if (loginToken == null)
                return false;
            IMongoCollection<Login> loginCollections = _db.GetCollection<Login>("Login");
            Login login = loginCollections.Find(currLogin => currLogin.Logintoken == loginToken).FirstOrDefault();
            return login != null;
        }
    }
}
