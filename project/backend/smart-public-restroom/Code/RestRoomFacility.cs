using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace smart_public_restroom.Code
{
    public class RestRoomFacility
    {
        public string id { get; set; }
        public List<Restroom> restRoomsInfo { get; set; } = new List<Restroom>();
        public bool smokeDetected { get; set; }
        public List<int> trashCapacities { get; set; }
        public List<int> soapDispensersCapacities { get; set; }

        public string address;

        public RestRoomFacility(){}

        public RestRoomFacility(string id, List<Restroom> restRoomsInfo, bool smokeDetected, List<int> trashCapacities, List<int> soapDispensersCapacities, string address)
        {
            this.id = id;
            this.restRoomsInfo = restRoomsInfo;
            this.smokeDetected = smokeDetected;
            this.trashCapacities = trashCapacities;
            this.soapDispensersCapacities = soapDispensersCapacities;
            this.address = address;
        }
    }
}
