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
        public class LoginResult
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
        public ActionResult<LoginResult> GetToilets(LoginData loginData)
        {
            bool exists = PublicRestroomsContext.User.Any(user => loginData.username == user.Username && PasswordHash.ValidatePassword(loginData.password, user.Password));
            return new LoginResult();
        }

        [Route("register")]
        [HttpPost]
        public ActionResult<LoginResult> Register(LoginData loginData)
        {
            PublicRestroomsContext.User.Add(new Models.User()
            {
                Username = loginData.username,
                Password = PasswordHash.HashPassword(loginData.password)
            });
            return new LoginResult();
        }
    }
}
