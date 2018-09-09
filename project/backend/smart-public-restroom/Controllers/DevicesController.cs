using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using smart_public_restroom.Code;

namespace smart_public_restroom.Controllers
{
    [Route("api/devices")]
    [ApiController]
    public class DeviceController : ControllerBase
    {
        // POST api/values
        [HttpPost]
        [Route("send")]
        public bool sendInfo([FromBody] RestRoomFacility data)
        {
            
            return true;
        }
    }
}
