using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;
using System;
using System.Collections.Generic;

namespace smartpublicrestroom.Models
{
    public partial class User
    {
        public ObjectId _id { get; set; }
        
        // external Id, easier to reference: 1,2,3 or A, B, C etc.
        public string username { get; set; }

        public string password { get; set; } 

        public string fullname { get; set; }
        public User(ObjectId id, string username, string password, string fullname)
        {
            _id = id;
            this.username = username;
            this.password = password;
            this.fullname = fullname;
        }
    }
}
