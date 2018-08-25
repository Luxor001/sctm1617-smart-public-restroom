using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace smart_public_restroom.Code
{
    public class Restroom
    {   
        public int id { get; set; }
        public bool paperAvaiable { get; set; }
        public int umidity { get; set; }
        public bool lightWorking { get; set; }
        public bool closed { get; set; }

        public Restroom() {}
        public Restroom(int id, bool paperAvaiable, int umidity, bool lightWorking, bool closed)
        {
            this.id = id;
            this.paperAvaiable = paperAvaiable;
            this.umidity = umidity;
            this.lightWorking = lightWorking;
            this.closed = closed;
        }
    }
}
