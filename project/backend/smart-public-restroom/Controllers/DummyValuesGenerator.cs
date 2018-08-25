using smart_public_restroom.Code;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace smart_public_restroom.Controllers
{
    public static class DummyValuesGenerator
    {
        private static double[][] adresses = {
                new double[] { 44.06747087,12.56445526 },
                new double[] { 44.0676483,12.5616545 },
                new double[] { 44.05944783,12.55891175},
                new double[] { 44.06821296,12.56218534},
                new double[] { 44.06852764, 12.56822782d }
        };
        public static List<RestRoomFacility> getDummyFacilities()
        {
            Random a = new Random();

            List<RestRoomFacility> facilities = Enumerable.Range(0, 5).Select(value =>
            {
                List<Restroom> restRooms = new List<Restroom>();
                restRooms.Add(new Restroom(value, a.Next(0, 2) != 0, a.Next(0, 100), a.Next(0, 2) != 0, a.Next(0, 2) != 0));
                restRooms.Add(new Restroom(value, a.Next(0, 2) != 0, a.Next(0, 100), a.Next(0, 2) != 0, a.Next(0, 2) != 0));
                restRooms.Add(new Restroom(value, a.Next(0, 2) != 0, a.Next(0, 100), a.Next(0, 2) != 0, a.Next(0, 2) != 0));

                List<int> trashCapacities = Enumerable.Range(0, 3).Select(currvalue => a.Next(0, 101)).ToList();
                return new RestRoomFacility(value.ToString(), restRooms, false, trashCapacities, trashCapacities, adresses[value]);
            }).ToList();
            return facilities;
        }
    }
}
