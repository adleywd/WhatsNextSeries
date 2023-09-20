using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata.Builders;
using WhatsNextSeries.Database.Entities;
using WhatsNextSeries.Models;

namespace WhatsNextSeries.Database;

public class WhatsNextMappingConfiguration : IEntityTypeConfiguration<Favorite>
{
    public void Configure(EntityTypeBuilder<Favorite> builder)
    {
        builder.ToTable(nameof(Favorite)).HasKey(k => k.Id);
        
        builder.HasIndex(favorite => favorite.TvShowId).IsUnique();
        
        builder.Property(favorite => favorite.TvShowId).IsRequired();
        builder.Property(favorite => favorite.Name).IsRequired();
        builder.Property(favorite => favorite.InProduction).IsRequired();
    }
}