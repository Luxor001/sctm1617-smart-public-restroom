using MongoDB.Bson;
using smartpublicrestroom.Code;
using smartpublicrestroom.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace smartpublicrestroom.Controllers
{
    public static class DummyValuesGenerator
    {
        private static string[][] adresses = {
                new string[] { "44.06747087","12.56445526" },
                new string[] { "44.0676483", "12.5616545" },
                new string[] { "44.05944783","12.55891175"},
                new string[] { "44.06821296","12.56218534"},
                new string[] { "44.06852764", "12.56822782d" }
        };
        private static string[] cityAdresses = {
                "Via Inesistente, 14",
                "Via Qualunque Qualunquelli, 18",
                "Via Tiziano Monsignor, 29",
                "Via Fantasia Fantasiosa, 2",
                "Via Iventata, 13"
        };
        private static string[] companies = {
                "F.lli Tizi S.r.l",
                "Pallini pinchi S.a.s",
                "Tizielli pallini S.r.l",
                "La splendente s.n.c",
                "Gertrumilde e soci"
        };
        public static List<RestRoom> getDummyFacilities()
        {
            Random a = new Random();
            List<RestRoom> facilities = facilities = Enumerable.Range(0, 5).Select(value =>
            {
                List<RestRoom> restRooms = new List<RestRoom>();

                string uid = "36e451a2-3520-4704-aec7-a9cfb5fb03b" + value;
                RestRoom restRoom = new RestRoom(uid, adresses[value], cityAdresses[value], companies[value], "");

                restRoom.sensorData = new RestroomInfo();
                restRoom.sensorData.trashCapacities = Enumerable.Range(0, 3).Select(currvalue => a.Next(0, 101)).ToList();
                restRoom.sensorData.soapDispensersCapacities = Enumerable.Range(0, 3).Select(currvalue => a.Next(0, 101)).ToList();
                restRoom.sensorData.smokeDetected = false;

                for (int i = 0; i < 3; i++)
                    restRoom.sensorData.roomsInfo.Add(new RoomInfo(i, a.Next(0, 2) != 0, a.Next(0, 100), a.Next(0, 2) != 0, a.Next(0, 2) != 0));

                return restRoom;
            }).ToList();

            return facilities;
        }
    }
}
