using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using smartpublicrestroom.Code;
using smartpublicrestroom.Models;

namespace smartpublicrestroom.Controllers
{
    [Route("api/devices")]
    [ApiController]
    public class DeviceController : ControllerBase
    {
        // POST api/values
        [HttpPost]
        [Route("send")]
        public bool sendInfo([FromBody] RestRoom data)
        {
            
            return true;
        }
    }
}
