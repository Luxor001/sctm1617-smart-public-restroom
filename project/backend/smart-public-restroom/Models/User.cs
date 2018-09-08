using System;
using System.Collections.Generic;

namespace smart_public_restroom.Models
{
    public partial class User
    {
        public string Username { get; set; }
        public string Password { get; set; }

        public User UsernameNavigation { get; set; }
        public User InverseUsernameNavigation { get; set; }
    }
}
