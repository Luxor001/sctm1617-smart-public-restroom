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

        [Route("login")]
        [HttpPost]
        public ActionResult<LoginResult> Login(LoginData loginData)
        {
           /* LoginResult result = new LoginResult();
            bool login = PublicRestroomsContext.User.Any(user => loginData.username == user.Username && PasswordHash.ValidatePassword(loginData.password, user.Password));
            if (!login)
            {
                result.message = "username or password incorrect";
                return result;
            }
            
            PublicRestroomsContext.Login.Add(new Models.Login()
            {
                Username = loginData.username,
                Logintoken = Guid.NewGuid().ToString(),
                Timestamp = new byte[2]
            });*/
            return new LoginResult();
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

            result.result = true;
            return result;
        }
    }
}
