package br.com.adley.myseriesproject.models;

/**
 * Created by Adley on 21/04/2016.
 * Model for Episodes
 */
public class TVShowSeasonEpisodes {
    // TODO: List of person(crew and guestStars).
    private String mAirDate;
    //private List<Person> mCrew;
    private int mEpisodeNumber;
    //private List<Person> mGuestStars;
    private String mEpisodeName;
    private String mEpisodeOverview;
    private int mEpisodeId;
    private String mEpisodeProductionCode;
    private int mEpisodeSeasonNumber;
    private String mEpisodeStillPath;
    private float mEpisodeVoteAverage;
    private float mEpisodeVoteCount;

    public TVShowSeasonEpisodes(String airDate, int episodeNumber, String episodeName,
                                String episodeOverview, int episodeId, String episodeProductionCode, int episodeSeasonNumber,
                                String episodeStillPath, float episodeVoteAverage, float episodeVoteCount) {
        mAirDate = airDate;
        mEpisodeNumber = episodeNumber;
        mEpisodeName = episodeName;
        mEpisodeOverview = episodeOverview;
        mEpisodeId = episodeId;
        mEpisodeProductionCode = episodeProductionCode;
        mEpisodeSeasonNumber = episodeSeasonNumber;
        mEpisodeStillPath = episodeStillPath;
        mEpisodeVoteAverage = episodeVoteAverage;
        mEpisodeVoteCount = episodeVoteCount;
    }

    public String getAirDate() {
        return mAirDate;
    }

    public void setAirDate(String airDate) {
        mAirDate = airDate;
    }

    public int getEpisodeNumber() {
        return mEpisodeNumber;
    }

    public void setEpisodeNumber(int episodeNumber) {
        mEpisodeNumber = episodeNumber;
    }

    public String getEpisodeName() {
        return mEpisodeName;
    }

    public void setEpisodeName(String episodeName) {
        mEpisodeName = episodeName;
    }

    public String getEpisodeOverview() {
        return mEpisodeOverview;
    }

    public void setEpisodeOverview(String episodeOverview) {
        mEpisodeOverview = episodeOverview;
    }

    public int getEpisodeId() {
        return mEpisodeId;
    }

    public void setEpisodeId(int episodeId) {
        mEpisodeId = episodeId;
    }

    public String getEpisodeProductionCode() {
        return mEpisodeProductionCode;
    }

    public void setEpisodeProductionCode(String episodeProductionCode) {
        mEpisodeProductionCode = episodeProductionCode;
    }

    public int getEpisodeSeasonNumber() {
        return mEpisodeSeasonNumber;
    }

    public void setEpisodeSeasonNumber(int episodeSeasonNumber) {
        mEpisodeSeasonNumber = episodeSeasonNumber;
    }

    public String getEpisodeStillPath() {
        return mEpisodeStillPath;
    }

    public void setEpisodeStillPath(String episodeStillPath) {
        mEpisodeStillPath = episodeStillPath;
    }

    public float getEpisodeVoteAverage() {
        return mEpisodeVoteAverage;
    }

    public void setEpisodeVoteAverage(float episodeVoteAverage) {
        mEpisodeVoteAverage = episodeVoteAverage;
    }

    public float getEpisodeVoteCount() {
        return mEpisodeVoteCount;
    }

    public void setEpisodeVoteCount(float episodeVoteCount) {
        mEpisodeVoteCount = episodeVoteCount;
    }

    @Override
    public String toString() {
        return "TVShowSeasonEpisodes{" +
                "mAirDate='" + mAirDate + '\'' +
                ", mEpisodeNumber=" + mEpisodeNumber +
                ", mEpisodeName='" + mEpisodeName + '\'' +
                ", mEpisodeOverview='" + mEpisodeOverview + '\'' +
                ", mEpisodeId=" + mEpisodeId +
                ", mEpisodeProductionCode=" + mEpisodeProductionCode +
                ", mEpisodeSeasonNumber=" + mEpisodeSeasonNumber +
                ", mEpisodeStillPath='" + mEpisodeStillPath + '\'' +
                ", mEpisodeVoteAverage=" + mEpisodeVoteAverage +
                ", mEpisodeVoteCount=" + mEpisodeVoteCount +
                '}';
    }
}
