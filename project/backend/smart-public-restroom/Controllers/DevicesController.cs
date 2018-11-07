using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using MongoDB.Driver;
using smartpublicrestroom.Code;
using smartpublicrestroom.Models;

namespace smartpublicrestroom.Controllers
{
    [Route("api/devices")]
    [ApiController]
    public class DeviceController : ControllerBase
    {
        private readonly IMongoDatabase _db;
        public DeviceController(IMongoDatabase db)
        {
            _db = db;
        }
        
        // POST api/values
        [HttpPost]
        [Route("send")]
        public string sendInfo([FromBody] RestroomInfo data)
        {
            BaseResult result = new BaseResult();
            RestRoom requestedRestroom = GetRequestedRestroom();
            if (requestedRestroom == null)
                return "FAIL";

            return "Found restroom";
            /*result.Result = true;
            return result;
            return true;*/
        }

        [HttpGet]
        [Route("testconn")]
        public string testConnection()
        {
            return "Test OK, connection OK";
        }
        
        public RestRoom GetRequestedRestroom()
        {
            string restroomGuid = Request.Headers["restroomuid"].FirstOrDefault();
            if (restroomGuid == null)
                return null;
            IMongoCollection<RestRoom> restRoomsCollection = _db.GetCollection<RestRoom>("Restroom");
            return restRoomsCollection.Find(currRestroom => currRestroom.uid == restroomGuid).FirstOrDefault();
        }
    }
}
