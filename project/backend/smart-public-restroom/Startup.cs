using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Hosting;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;

namespace smart_public_restroom
{
    public class Startup
    {
        public Startup(IConfiguration configuration)
        {
            Configuration = configuration;
        }

        public IConfiguration Configuration { get; }

        // This method gets called by the runtime. Use this method to add services to the container.
        public void ConfigureServices(IServiceCollection services)
        {
            services.AddMvc().SetCompatibilityVersion(CompatibilityVersion.Version_2_1);
        }

        // This method gets called by the runtime. Use this method to configure the HTTP request pipeline.
        public void Configure(IApplicationBuilder app, IHostingEnvironment env)
        {
            /* I know, huge security concerns. 
               But let me point straight something:
                1) This is a localhost-only project. It will never pass the unbereable firewalls of my router.
                2) No sensitive data ar involved in the project.
                3) This is an academic project about smart cities, not IT Security. 
                   I am fully aware of the implications of the settings below but i firmly believe they can considered "simplifying assumptions" for the scope of the course.

             I usually consider those implications as low risk-low impact :)   
             */
            app.UseCors(builder => builder.WithOrigins("http://localhost:4200")
                                  .AllowAnyMethod()
                                  .AllowAnyHeader());

            if (env.IsDevelopment())
            {
                app.UseDeveloperExceptionPage();
            }
            else
            {
                app.UseHsts();
            }

            app.UseHttpsRedirection(); 
            app.UseMvc();
            
        }
    }
}
