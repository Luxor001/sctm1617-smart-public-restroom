using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using MongoDB.Driver;
using smartpublicrestroom.Code;
using smartpublicrestroom.Models;
using static smartpublicrestroom.Controllers.ManagerController;

namespace smartpublicrestroom.Controllers
{
    [Route("api/data")]
    [ApiController]
    public class ConsumerController : ControllerBase
    {
        private readonly IMongoDatabase _db;
        public ConsumerController(IMongoDatabase db)
        {
            _db = db;
        }

        [Route("getRestrooms")]
        [HttpPost]
        public async Task<ActionResult<List<RestRoom>>> GetToilets()
        {
            IMongoCollection<Models.RestRoom> restroomsCollection = _db.GetCollection<Models.RestRoom>("Restroom");
            List<RestRoom> restrooms = restroomsCollection.AsQueryable().ToList();
            return restrooms;
        }
                
        [HttpPost]
        [Route("sendreport")]
        public BaseResult sendReport([FromBody] Report report)
        {
            AddRestroomResult result = new AddRestroomResult();

            IMongoCollection<Models.Report> reportCollections = _db.GetCollection<Models.Report>("Report");
            reportCollections.InsertOne(report);

            result.Result = true;
            return result;
        }
    }
}
