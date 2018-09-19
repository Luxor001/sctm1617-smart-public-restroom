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
            public Models.Restroom newRestroom { get; set; }
        }
        public class AddRestroomResult: BaseResult
        {
            public int restroomId { get; set; }
        }
        public class LoginData
        {
            public string username { get; set; }
            public string password { get; set; }
            public string loginToken { get; set; }
        }
        public class RegisterData : LoginData
        {
            public string fullname { get; set; }
        }
        public class LoginResult : BaseResult
        {
            public string loginToken { get; set; }
        }

        private readonly IMongoDatabase _db;
        public ManagerController(IMongoDatabase db)
        {
            _db = db;
        }

        [Route("getRestrooms")]
        [HttpPost] 
        public async Task<ActionResult<List<RestRoomFacility>>> GetToilets()
        {
            /*
            var cursor = await _db.ListCollectionNamesAsync();
            await cursor.ForEachAsync(db =>  Console.WriteLine(db[0]));
            IMongoCollection<User> users = _db.GetCollection<User>("User");

            var filter = new BsonDocument();
            var query = await users.FindAsync(filter);

            users.InsertOne(new User() { _id = ObjectId.GenerateNewId(), username = "Lux", password = "prova", fullname = "Luxor luxarelli" });*/
            return DummyValuesGenerator.getDummyFacilities();
        }

        [Route("addRestroom")]
        [HttpPost]
        public ActionResult<AddRestroomResult> AddRestroom(AddRestroomData addRestroomData)
        {
            AddRestroomResult result = new AddRestroomResult();

            IMongoCollection<Models.Restroom> restroomsCollection = _db.GetCollection<Models.Restroom>("Restroom");
            addRestroomData.newRestroom._id = ObjectId.GenerateNewId();
            restroomsCollection.InsertOne(addRestroomData.newRestroom);

            result.Result = true;
            return result;
        }

        [Route("login")]
        [HttpPost]
        public ActionResult<LoginResult> Login(LoginData loginData)
        {
            LoginResult result = new LoginResult();

            // username-password authentication
            if (loginData.loginToken == null)
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
            }

            // token-based authentication
            if (loginData.loginToken != null)
            {
                IMongoCollection<Login> loginCollections = _db.GetCollection<Login>("Login");
                Login login = loginCollections.Find(currLogin => currLogin.Logintoken == loginData.loginToken).FirstOrDefault();
                if (login == null)
                {
                    result.message = "wrong loginToken";
                    return result;
                }
                login.Timestamp = DateTime.Now;
                loginCollections.ReplaceOne(currLogin => currLogin._id == login._id, login);
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
    }
}
