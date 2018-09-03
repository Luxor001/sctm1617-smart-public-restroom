using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using smart_public_restroom.Code;

namespace smart_public_restroom.Controllers
{
    [Route("api/manager")]
    [ApiController]
    public class ManagerController : ControllerBase
    {
        [Route("getRestrooms")]
        [HttpPost] 
        public ActionResult<List<RestRoomFacility>> GetToilets()
        {
            return DummyValuesGenerator.getDummyFacilities();
        }
    }
}
