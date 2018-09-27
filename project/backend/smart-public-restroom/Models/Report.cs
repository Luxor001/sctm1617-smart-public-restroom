using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace smartpublicrestroom.Models
{
    public class Report
    {
        public ObjectId _id { get; set; }

        public string name { get; set; }
        public string comment { get; set; }

        [BsonDateTimeOptions]
        public DateTime Timestamp { get; set; } = DateTime.Now;

        public Report(ObjectId id, string name, string comment)
        {
            _id = id;
            this.name = name;
            this.comment = comment;
        }
    }
}
