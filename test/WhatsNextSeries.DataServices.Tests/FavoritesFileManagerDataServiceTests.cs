using Moq;
using Newtonsoft.Json;
using Shouldly;
using WhatsNextSeries.Models;

namespace WhatsNextSeries.DataServices.Tests
{
    public class FavoritesFileManagerDataServiceTests
    {
        private readonly string _favoritesFilePath = Path.Combine(Environment.CurrentDirectory, "Data/favorites.json");
        private readonly List<TvShowDetail> _testFavoritesData;

        public FavoritesFileManagerDataServiceTests()
        {
            _testFavoritesData = new List<TvShowDetail>
            {
                new() { Id = 1, Name = "Test Show 1" },
                new() { Id = 2, Name = "Test Show 2" },
                new() { Id = 3, Name = "Test Show 3" }
            };

            if (!Directory.Exists(Path.GetDirectoryName(_favoritesFilePath)))
            {
                Directory.CreateDirectory(Path.GetDirectoryName(_favoritesFilePath) ??
                                          throw new InvalidOperationException("Invalid favorites path"));
            }

            if (File.Exists(_favoritesFilePath))
            {
                File.Delete(_favoritesFilePath);
            }

            using var file = new StreamWriter(_favoritesFilePath);
            var jsonData = JsonConvert.SerializeObject(_testFavoritesData);
            file.Write(jsonData);
            file.Flush();
        }

        [Fact]
        public async Task LoadFavoritesTvShow_Success_ReturnsCorrectData()
        {
            // Arrange
            var tvShowFileManager = new FavoritesFileManagerDataService();

            // Act
            var result = await tvShowFileManager.LoadFavoritesTvShow(CancellationToken.None).ConfigureAwait(false);

            // Assert
            result.ShouldBeEquivalentTo(_testFavoritesData);
        }

        [Fact]
        public async Task SaveFavoriteTvShow_Success_SavesData()
        {
            // Arrange
            var tvShowDetail = new TvShowDetail { Id = 4, Name = "Test Show 4" };
            var tvShowDetail2 = new TvShowDetail { Id = 5, Name = "Test Show 5" };
            var expectedData = new List<TvShowDetail>(_testFavoritesData)
            {
                tvShowDetail,
                tvShowDetail2
            };

            var tvShowFileManager = new FavoritesFileManagerDataService();

            // Act
            var saveResult = await tvShowFileManager
                .SaveFavoriteTvShow(new List<TvShowDetail>{ tvShowDetail, tvShowDetail2 }, CancellationToken.None)
                .ConfigureAwait(false);
            var allFavoritesResult =
                await tvShowFileManager.LoadFavoritesTvShow(CancellationToken.None).ConfigureAwait(false);

            // Assert
            saveResult.ShouldBeTrue();
            expectedData.ShouldBeEquivalentTo(allFavoritesResult);
        }
    }
}