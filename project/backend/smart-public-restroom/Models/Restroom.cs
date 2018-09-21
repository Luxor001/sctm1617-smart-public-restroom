using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;
using System;
using System.Collections.Generic;

namespace smartpublicrestroom.Models
{
    public partial class Restroom
    {
        public ObjectId _id { get; set; }

        public string cityAddress { get; set; }
        public double[] address { get; set; }
        public string company { get; set; }
        public string device { get; set; }

        public RestroomInfo sensorData { get; set; }
        public Restroom(ObjectId id, double[] adress, string cityAddress, string company, string device)
        {
            _id = id;
            this.address = address;
            this.cityAddress = cityAddress;
            this.company = company;
            this.device = device;
        }
    }

    public partial class RestroomInfo
    {
        internal ObjectId _id { get; set; }
        public List<Restroom> roomsInfo { get; set; } = new List<Restroom>();
        public bool smokeDetected { get; set; }
        public List<int> trashCapacities { get; set; }
        public List<int> soapDispensersCapacities { get; set; }

        public RestroomInfo(ObjectId id, List<Restroom> restRoomsInfo, bool smokeDetected, List<int> trashCapacities, List<int> soapDispensersCapacities)
        {
            _id = id;
            this.roomsInfo = restRoomsInfo;
            this.smokeDetected = smokeDetected;
            this.trashCapacities = trashCapacities;
            this.soapDispensersCapacities = soapDispensersCapacities;
        }
    }

    public class RoomInfo
    {
        public int id { get; set; }
        public bool paperAvaiable { get; set; }
        public int umidity { get; set; }
        public bool lightWorking { get; set; }
        public bool closed { get; set; }

        public RoomInfo() { }
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
