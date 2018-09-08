using System;
using System.Collections.Generic;

namespace smart_public_restroom.Models
{
    public partial class Login
    {
        public string Username { get; set; }
        public string LoginToken { get; set; }
        public byte[] LastAccess { get; set; }
    }
}
