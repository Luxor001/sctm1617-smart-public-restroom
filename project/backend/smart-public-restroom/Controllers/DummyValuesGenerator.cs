using smart_public_restroom.Code;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace smart_public_restroom.Controllers
{
    public static class DummyValuesGenerator
    {
        
        public static List<RestRoomFacility> getDummyFacilities()
        {
            Random a = new Random();

            List<RestRoomFacility> facilities = Enumerable.Range(0, 10).Select(value =>
            {
                List<Restroom> restRooms = new List<Restroom>();
                restRooms.Add(new Restroom(value, a.Next(0, 2) != 0, a.Next(0, 100), a.Next(0, 2) != 0, a.Next(0, 2) != 0));
                restRooms.Add(new Restroom(value, a.Next(0, 2) != 0, a.Next(0, 100), a.Next(0, 2) != 0, a.Next(0, 2) != 0));
                restRooms.Add(new Restroom(value, a.Next(0, 2) != 0, a.Next(0, 100), a.Next(0, 2) != 0, a.Next(0, 2) != 0));

                List<int> trashCapacities = Enumerable.Range(0, 3).Select(currvalue => a.Next(0, 101)).ToList();
                    return new RestRoomFacility(value.ToString(), restRooms, false, trashCapacities, trashCapacities, "random Address 42, RandomCity");
                }).ToList();
            return facilities;
        }
    }
}
