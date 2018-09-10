using System;
using System.Collections.Generic;

namespace smartpublicrestroom.Models
{
    public partial class Login
    {
        public string Username { get; set; }
        public string Logintoken { get; set; }
        public byte[] Timestamp { get; set; }
    }
}
