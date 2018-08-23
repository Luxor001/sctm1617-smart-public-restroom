using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using smart_public_restroom.Code;

namespace smart_public_restroom.Controllers
{
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

        [Route("getRestrooms")]
        [HttpPost] /*da aggiungere come parametro: [FromBody] string coordinates*/
        public ActionResult<List<RestRoomFacility>> GetToilets()
        {
            //TODO: query toilets based on location of user

            return DummyValuesGenerator.getDummyFacilities();
        }

        // POST api/values
        [HttpPost]
        [Route("send")]
        public bool sendInfo([FromBody] RestRoomFacility data)
        {
            //TODO: write down to DB...



            return true;
        }
    }
}
