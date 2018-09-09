using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using smart_public_restroom.Code;
using smart_public_restroom.Models;

namespace smart_public_restroom.Controllers
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
        public class LoginResult : BaseResult
        {
            public string loginToken { get; set; }
        }
        private PublicRestroomsContext PublicRestroomsContext;

        public ManagerController(PublicRestroomsContext context)
        {
            PublicRestroomsContext = context;
        }

        [Route("getRestrooms")]
        [HttpPost] 
        public ActionResult<List<RestRoomFacility>> GetToilets()
        {
            return DummyValuesGenerator.getDummyFacilities();
        }

        [Route("login")]
        [HttpPost]
        public ActionResult<LoginResult> Login(LoginData loginData)
        {
            LoginResult result = new LoginResult();
            bool login = PublicRestroomsContext.User.Any(user => loginData.username == user.Username && PasswordHash.ValidatePassword(loginData.password, user.Password));
            if (!login)
            {
                result.message = "username or password incorrect";
                return result;
            }

            PublicRestroomsContext.Login.Add(new Models.Login()
            {
                Username = loginData.username,
                LoginToken = Guid.NewGuid().ToString(),
                LastAccess = new byte[2]
            });
            return new LoginResult();
        }

        [Route("register")]
        [HttpPost]
        public ActionResult<LoginResult> Register(LoginData loginData)
        {
            LoginResult result = new LoginResult();
            // If some user is already registered with that given username...
            if(PublicRestroomsContext.User.Any(user => loginData.username == user.Username))
            {
                result.message = "username already in use";
                return result;
            }

            PublicRestroomsContext.User.Add(new Models.User()
            {
                Username = loginData.username,
                Password = PasswordHash.HashPassword(loginData.password)
            });

            PublicRestroomsContext.Login.Add(new Models.Login()
            {
                Username = loginData.username,
                LoginToken = Guid.NewGuid().ToString(),
                LastAccess = new byte[2]
            });

            result.result = true;
            return result;
        }
    }
}
