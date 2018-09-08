using System;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata;

namespace smart_public_restroom.Models
{
    public partial class PublicRestroomsContext : DbContext
    {
        public PublicRestroomsContext()
        {
        }

        public PublicRestroomsContext(DbContextOptions<PublicRestroomsContext> options)
            : base(options)
        {
        }

        public virtual DbSet<Login> Login { get; set; }
        public virtual DbSet<User> User { get; set; }

        protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
        {

        }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            modelBuilder.Entity<Login>(entity =>
            {
                entity.HasKey(e => new { e.Username, e.LoginToken });

                entity.Property(e => e.Username)
                    .HasColumnName("username")
                    .HasMaxLength(20);

                entity.Property(e => e.LoginToken)
                    .HasColumnName("loginToken")
                    .HasMaxLength(50)
                    .IsUnicode(false);

                entity.Property(e => e.LastAccess)
                    .IsRequired()
                    .HasColumnName("lastAccess")
                    .IsRowVersion();
            });

            modelBuilder.Entity<User>(entity =>
            {
                entity.HasKey(e => e.Username);

                entity.Property(e => e.Username)
                    .HasColumnName("username")
                    .HasMaxLength(20)
                    .ValueGeneratedNever();

                entity.Property(e => e.Password)
                    .IsRequired()
                    .HasColumnName("password")
                    .HasMaxLength(20);

                entity.HasOne(d => d.UsernameNavigation)
                    .WithOne(p => p.InverseUsernameNavigation)
                    .HasForeignKey<User>(d => d.Username)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("FK_User_User1");
            });
        }
    }
}
