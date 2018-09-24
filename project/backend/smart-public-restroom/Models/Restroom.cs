using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;
using MongoDB.Bson.Serialization.IdGenerators;
using System;
using System.Collections.Generic;

namespace smartpublicrestroom.Models
{
    public partial class RestRoom
    {
        [BsonId(IdGenerator = typeof(StringObjectIdGenerator))]
        public string uid { get; set; }

        public string cityAddress { get; set; }
        public string[] address { get; set; }
        public string company { get; set; }
        public string device { get; set; }

        public RestroomInfo sensorData { get; set; }
        public RestRoom(string uid, string[] address, string cityAddress, string company, string device)
        {
            this.uid = uid;
            this.address = address;
            this.cityAddress = cityAddress;
            this.company = company;
            this.device = device;
        }
    }

    public partial class RestroomInfo
    {
        [BsonId]
        internal ObjectId _id { get; set; }
        public List<RoomInfo> roomsInfo { get; set; } = new List<RoomInfo>();
        public bool smokeDetected { get; set; }
        public List<int> trashCapacities { get; set; }
        public List<int> soapDispensersCapacities { get; set; }

        public RestroomInfo(List<RoomInfo> restRoomsInfo, bool smokeDetected, List<int> trashCapacities, List<int> soapDispensersCapacities)
        {
            this._id = ObjectId.GenerateNewId();
            this.roomsInfo = restRoomsInfo;
            this.smokeDetected = smokeDetected;
            this.trashCapacities = trashCapacities;
            this.soapDispensersCapacities = soapDispensersCapacities;
        }
        public RestroomInfo()
        {
            this._id = ObjectId.GenerateNewId();
        }
    }

    public class RoomInfo
    {
        [BsonId]
        internal int id { get; set; }
        public bool paperAvaiable { get; set; }
        public int umidity { get; set; }
        public bool lightWorking { get; set; }
        public bool closed { get; set; }

        public RoomInfo()
        {

        }
        public RoomInfo(int id, bool paperAvaiable, int umidity, bool lightWorking, bool closed)
        {
            this.id = id;
            this.paperAvaiable = paperAvaiable;
            this.umidity = umidity;
            this.lightWorking = lightWorking;
            this.closed = closed;
        }
    }
}
