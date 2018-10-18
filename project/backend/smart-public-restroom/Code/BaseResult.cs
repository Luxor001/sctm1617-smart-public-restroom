using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace smartpublicrestroom.Code
{
    public abstract class BaseResult
    {
        public bool Result { get; set; }
        public string message { get; set; }
    }
}
