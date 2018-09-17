using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;
using System;
using System.Collections.Generic;

namespace smartpublicrestroom.Models
{
    public partial class Login
    {
        public ObjectId _id { get; set; }

        public User user { get; set; }
        public string Logintoken { get; set; }

        [BsonDateTimeOptions]
        public DateTime Timestamp { get; set; } = DateTime.Now;

        public Login(ObjectId id, User user, string logintoken)
        {
            _id = id;
            this.user = user;
            Logintoken = logintoken;
            Timestamp = DateTime.Now;
        }
    }
}
