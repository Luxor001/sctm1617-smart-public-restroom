using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;

namespace smart_public_restroom.Controllers
{
    public class RestroomFacilityInfo
    {
        public int id { get; set; }
        public List<RestroomInfo> restRoomsInfo { get; set; } = new List<RestroomInfo>();
        public bool smokeDetected { get; set; }
        public List<int> trashCapacities { get; set; }
        public List<int> soapDispensersCapacities { get; set; }
    }

    public class RestroomInfo
    {
        public int id { get; set; }
        public bool paperAvaiable { get; set; }
        public int umidity { get; set; }
        public bool lightWorking { get; set; }
        public bool closed { get; set; }
    }

    [Route("api/data")]
    [ApiController]
    public class DataController : ControllerBase
    {
        [Route("values")]
        [HttpGet]
        public ActionResult<IEnumerable<string>> Get()
        {
            return new string[] { "value1", "value2" };
        }

        // POST api/values
        [HttpPost]
        public string sendInfo([FromBody] RestroomFacilityInfo value)
        {
            return "works!";   
        }
    }
}
