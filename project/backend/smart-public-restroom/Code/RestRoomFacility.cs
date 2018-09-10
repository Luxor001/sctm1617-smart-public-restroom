using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace smartpublicrestroom.Code
{
    public class RestRoomFacility
    {
        public string id { get; set; }
        public List<Restroom> restRoomsInfo { get; set; } = new List<Restroom>();
        public bool smokeDetected { get; set; }
        public List<int> trashCapacities { get; set; }
        public List<int> soapDispensersCapacities { get; set; }

        public double[] address;
        public string cityAddress { get; set; }
        public string company { get; set; }

        public RestRoomFacility(){}

        public RestRoomFacility(string id, List<Restroom> restRoomsInfo, bool smokeDetected, List<int> trashCapacities, List<int> soapDispensersCapacities, double[] address, string cityAddress, string company)
        {
            this.id = id;
            this.restRoomsInfo = restRoomsInfo;
            this.smokeDetected = smokeDetected;
            this.trashCapacities = trashCapacities;
            this.soapDispensersCapacities = soapDispensersCapacities;
            this.address = address;
            this.cityAddress = cityAddress;
            this.company = company;
        }
    }
}
