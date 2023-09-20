using Microsoft.EntityFrameworkCore;
using WhatsNextSeries.Database.Entities;

namespace WhatsNextSeries.Database;

public class WhatsNextDbContext : DbContext
{
    /// <summary>
    /// Tv Show Details Table
    /// </summary>
    public DbSet<Favorite> Favorites { get; set; }

    /// <summary>
    /// Database path with sqlite filename
    /// </summary>
    private readonly string _databasePath = Path.Combine(Environment.CurrentDirectory, DatabaseName);
    
    private const string DatabaseName = "WhatsNextSeries.db";
    
    public WhatsNextDbContext()
    {
        if(OperatingSystem.IsAndroid() || OperatingSystem.IsIOS())
        {
            _databasePath = Path.Combine(Environment.GetFolderPath(Environment.SpecialFolder.LocalApplicationData), DatabaseName);
        }
    }
    
    protected override void OnModelCreating(ModelBuilder modelBuilder)
    {
        modelBuilder.ApplyConfiguration(new WhatsNextMappingConfiguration());
        
        base.OnModelCreating(modelBuilder);
    }

    protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
    {
        base.OnConfiguring(optionsBuilder);
        var connectionString = $"data source={_databasePath}";
        optionsBuilder.UseSqlite(connectionString);
    }
    
}