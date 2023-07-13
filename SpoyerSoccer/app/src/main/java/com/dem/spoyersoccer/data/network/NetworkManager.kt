package com.dem.spoyersoccer.data.network

import android.util.Log
import com.dem.spoyersoccer.models.Event
import com.dem.spoyersoccer.models.EventItemModel
import com.dem.spoyersoccer.models.League
import com.dem.spoyersoccer.models.LiveMatch
import com.dem.spoyersoccer.models.LiveMatchOdds
import com.dem.spoyersoccer.models.Odds
import com.dem.spoyersoccer.models.PastMatch
import com.dem.spoyersoccer.models.StatisticsModel
import com.dem.spoyersoccer.models.Stats
import com.dem.spoyersoccer.models.Team
import com.dem.spoyersoccer.models.UpcomingMatch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.net.URL
import java.util.Date

class NetworkManager {

    private val url = "https://spoyer.com/api/en/get.php?login=boop1337&token=46559-KEYlKW1aM1MWiNN&task="
    private val imageUrl = "https://spoyer.com/api/team_img/soccer/"

    suspend fun getLeagues(): List<League> {
        val leaguesUrl = url.plus("enddata&sport=soccer&day=today")
        return try {
            val leaguesData = fetchDataFromUrl(leaguesUrl)
            val leagues = parseLeaguesResponse(leaguesData)
            leagues
            //leagues.take(20)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    private suspend fun fetchDataFromUrl(apiUrl: String): String {
        return withContext(Dispatchers.IO) {
            try {
                URL(apiUrl).readText()
            } catch (e: IOException) {
                e.printStackTrace()
                ""
            }
        }
    }

    private fun parseLeaguesResponse(response: String): List<League> {
        val leagues = mutableListOf<League>()

        val jsonResponse = JSONObject(response)
        val gamesEndArray = jsonResponse.getJSONArray("games_end")
        for (i in 0 until gamesEndArray.length()) {
            val gameObj = gamesEndArray.getJSONObject(i)
            val gameId = gameObj.getString("game_id").toInt()
            val leagueObj = gameObj.getJSONObject("league")
            val leagueId = leagueObj.getString("id").toInt()
            val leagueName = leagueObj.getString("name")
            val names = leagues.map { it.name }
            if (!names.contains(leagueName)) {
                leagues.add(League(leagueId, leagueName, gameId))
            }

            if (leagues.size >= 20) {
                break
            }
        }

        return leagues
    }

    suspend fun getTeams(leagueId: Int): List<Team> {
        return try {
            val leagueInfoUrl = url.plus("tabledata&league=$leagueId")
            val leagueInfoData = fetchDataFromUrl(leagueInfoUrl)
            parseTeamsResponse(leagueInfoData)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    private fun parseTeamsResponse(response: String): List<Team> {
        val teams = mutableListOf<Team>()

        val jsonResponse = JSONObject(response)
        val resultsObj = jsonResponse.getJSONObject("results")
        val overallObj = resultsObj.getJSONObject("overall")
        val tablesArray = overallObj.getJSONArray("tables")
        for (i in 0 until tablesArray.length()) {
            val tableObj = tablesArray.getJSONObject(i)
            val rowsArray = tableObj.getJSONArray("rows")
            for (j in 0 until rowsArray.length()) {
                if (teams.size >= 20) {
                    break
                }
                val rowObj = rowsArray.getJSONObject(j)
                val teamObj = rowObj.getJSONObject("team")
                val teamId = teamObj.getString("id").toInt()
                val teamName = teamObj.getString("name")
                val imageId = teamObj.getString("image_id")
                val imageUrl = "$imageUrl$imageId.png"
                teams.add(Team(teamId, teamName, imageUrl))
            }
            if (teams.size >= 20) {
                break
            }
        }

        return teams
    }

    suspend fun getFinishedMatches(): List<PastMatch> {
        val matchesUrl = url.plus("enddata&sport=soccer&day=today")
        val matchesData = fetchDataFromUrl(matchesUrl)
        return parseFinishedMatchesResponse(matchesData)
    }

    private fun parseFinishedMatchesResponse(response: String): List<PastMatch> {
        val matches = mutableListOf<PastMatch>()

        val jsonResponse = JSONObject(response)
        val gamesEndArray = jsonResponse.getJSONArray("games_end")
        for (i in 0 until gamesEndArray.length()) {
            if (matches.size >= 30) {
                break
            }
            val gameObj = gamesEndArray.getJSONObject(i)
            val matchId = gameObj.getString("game_id").toInt()
            val time = gameObj.getString("time").toLong() * 1000
            val timeStatus = gameObj.getString("time_status").toInt()
            val leagueObj = gameObj.getJSONObject("league")
            val leagueName = leagueObj.getString("name")
            val homeTeamObj = gameObj.getJSONObject("home")
            val homeTeamId = homeTeamObj.getString("id").toInt()
            val homeTeamName = homeTeamObj.getString("name")
            val homeImageId = homeTeamObj.getString("image_id")
            val homeImageUrl = "$imageUrl$homeImageId.png"
            val homeTeam = Team(homeTeamId, homeTeamName, homeImageUrl)
            val awayTeamObj = gameObj.getJSONObject("away")
            val awayTeamId = awayTeamObj.getString("id").toInt()
            val awayTeamName = awayTeamObj.getString("name")
            val awayImageId = awayTeamObj.getString("image_id")
            val awayImageUrl = "$imageUrl$awayImageId.png"
            val awayTeam = Team(awayTeamId, awayTeamName, awayImageUrl)
            val score = gameObj.getString("score")
            if (score.isNotBlank()) {
                val pastMatch = PastMatch(matchId, homeTeam, awayTeam, score, timeStatus, Date(time))
                matches.add(pastMatch)
            }
        }

        return matches
    }

    suspend fun getUpcomingMatches(): List<UpcomingMatch> {
        val matchesUrl = url.plus("predata&sport=soccer&day=today")
        val matchesData = fetchDataFromUrl(matchesUrl)
        return parseUpcomingMatchesResponse(matchesData)
    }

    private fun parseUpcomingMatchesResponse(response: String): List<UpcomingMatch> {
        val matches = mutableListOf<UpcomingMatch>()

        val jsonResponse = JSONObject(response)
        val gamesEndArray = jsonResponse.getJSONArray("games_pre")
        for (i in 0 until gamesEndArray.length()) {
            if (matches.size >= 30) {
                break
            }
            val gameObj = gamesEndArray.getJSONObject(i)
            val matchId = gameObj.getString("game_id").toInt()
            val time = gameObj.getString("time").toLong() * 1000
            val timeStatus = gameObj.getString("time_status").toInt()
            val leagueObj = gameObj.getJSONObject("league")
            val leagueName = leagueObj.getString("name")
            val homeTeamObj = gameObj.getJSONObject("home")
            val homeTeamId = homeTeamObj.getString("id").toInt()
            val homeTeamName = homeTeamObj.getString("name")
            val homeImageId = homeTeamObj.getString("image_id")
            val homeImageUrl = "$imageUrl$homeImageId.png"
            val homeTeam = Team(homeTeamId, homeTeamName, homeImageUrl)
            val awayTeamObj = gameObj.getJSONObject("away")
            val awayTeamId = awayTeamObj.getString("id").toInt()
            val awayTeamName = awayTeamObj.getString("name")
            val awayImageId = awayTeamObj.getString("image_id")
            val awayImageUrl = "$imageUrl$awayImageId.png"
            val awayTeam = Team(awayTeamId, awayTeamName, awayImageUrl)
            val bet365IdStr = gameObj.getString("bet365_id")
            if (bet365IdStr.isNotBlank()) {
                val bet365Id = bet365IdStr.toInt()
                val pastMatch = UpcomingMatch(matchId, homeTeam, awayTeam, Date(time), bet365Id)
                matches.add(pastMatch)
            }
        }

        return matches
    }

    suspend fun getLiveMatches(): List<LiveMatch> {
        val liveUrl = url.plus("livedata&sport=soccer")
        val liveData = fetchDataFromUrl(liveUrl)
        return parseLiveMatchesResponse(liveData)
    }

    private fun parseLiveMatchesResponse(response: String): List<LiveMatch> {
        val liveMatches = mutableListOf<LiveMatch>()

        val jsonResponse = JSONObject(response)
        val gamesLiveArray = jsonResponse.getJSONArray("games_live")
        for (i in 0 until gamesLiveArray.length()) {
            val gameObj = gamesLiveArray.getJSONObject(i)
            val matchId = gameObj.getString("game_id").toInt()
            val homeTeam = gameObj.getJSONObject("home")
            val homeTeamId = homeTeam.getString("id").toInt()
            val homeTeamName = homeTeam.getString("name")
            val homeImageId = homeTeam.optString("image_id")
            val homeImageUrl = if (!homeImageId.isNullOrEmpty()) "$imageUrl$homeImageId.png" else ""
            val homeTeamObject = Team(homeTeamId, homeTeamName, homeImageUrl)

            val awayTeam = gameObj.getJSONObject("away")
            val awayTeamId = awayTeam.getString("id").toInt()
            val awayTeamName = awayTeam.getString("name")
            val awayImageId = awayTeam.optString("image_id")
            val awayImageUrl = if (!awayImageId.isNullOrEmpty()) "$imageUrl$awayImageId.png" else ""
            val awayTeamObject = Team(awayTeamId, awayTeamName, awayImageUrl)

            val league = gameObj.getJSONObject("league").getString("name")
            val score = gameObj.getString("score")
            val timeStatus = gameObj.getString("time_status").toInt()
            val time = gameObj.getString("time").toLong() * 1000
            val timer = gameObj.getString("timer")
            val bet365Id = gameObj.getString("bet365_id").toInt()

            val liveMatch = LiveMatch(matchId, homeTeamObject, awayTeamObject, league, score, timeStatus, Date(time), timer, bet365Id)
            liveMatches.add(liveMatch)

            if (liveMatches.size >= 20) {
                break
            }
        }

        return liveMatches
    }

    suspend fun getMatchOdds(gameId: Int): LiveMatchOdds {
        val oddsUrl = url.plus("preodds&bookmaker=bet365&game_id=$gameId")
        val oddsData = fetchDataFromUrl(oddsUrl)
        val data = parseMatchOddsResponse(oddsData)
        return data
    }

    private fun parseMatchOddsResponse(response: String): LiveMatchOdds {
        val jsonResponse = JSONObject(response)
        val resultsArray = jsonResponse.getJSONArray("results")
        val resultsObj = resultsArray.getJSONObject(0)
        val id = resultsObj.getString("event_id").toInt()
        val mainObj = resultsObj.getJSONObject("main")
        val fullTimeResultArray = mainObj.getJSONObject("sp").getJSONArray("full_time_result")

        val homeOddsObj = fullTimeResultArray.getJSONObject(0)
        val homeOddsId = homeOddsObj.getString("id").toInt()
        val homeOddsValue = homeOddsObj.getString("odds").toDouble()
        val homeOddsName = homeOddsObj.getString("name")
        val homeOdds = Odds(homeOddsId, homeOddsValue, homeOddsName)

        val drawOddsObj = fullTimeResultArray.getJSONObject(1)
        val drawOddsId = drawOddsObj.getString("id").toInt()
        val drawOddsValue = drawOddsObj.getString("odds").toDouble()
        val drawOddsName = drawOddsObj.getString("name")
        val drawOdds = Odds(drawOddsId, drawOddsValue, drawOddsName)

        val awayOddsObj = fullTimeResultArray.getJSONObject(2)
        val awayOddsId = awayOddsObj.getString("id").toInt()
        val awayOddsValue = awayOddsObj.getString("odds").toDouble()
        val awayOddsName = awayOddsObj.getString("name")
        val awayOdds = Odds(awayOddsId, awayOddsValue, awayOddsName)

        return LiveMatchOdds(id, homeOdds, drawOdds, awayOdds)
    }

    suspend fun getEvent(eventId: Int): Event? {
        val eventUrl = url.plus("eventdata&game_id=$eventId")
        val eventData = fetchDataFromUrl(eventUrl)
        val data = parseEventResponse(eventData)
        return data
    }

    private fun parseEventResponse(response: String): Event? {
        val jsonResponse = JSONObject(response)
        val resultsArray = jsonResponse.getJSONArray("results")
        if (resultsArray.length() != 0) {
            val resultsObj = resultsArray.getJSONObject(0)
            val eventId = resultsObj.getString("id").toInt()
            val time = resultsObj.getString("time").toLong() * 1000
            val timeStatus = resultsObj.getString("time_status").toInt()
            val leagueObj = resultsObj.getJSONObject("league")
            val leagueId = leagueObj.getString("id").toInt()
            val leagueName = leagueObj.getString("name")
            val homeTeamObj = resultsObj.getJSONObject("home")
            val homeTeamId = homeTeamObj.getString("id").toInt()
            val homeTeamName = homeTeamObj.getString("name")
            val homeImageId = homeTeamObj.getString("image_id")
            val homeImageUrl = "$imageUrl$homeImageId.png"
            val homeTeam = Team(homeTeamId, homeTeamName, homeImageUrl)
            val awayTeamObj = resultsObj.getJSONObject("away")
            val awayTeamId = awayTeamObj.getString("id").toInt()
            val awayTeamName = awayTeamObj.getString("name")
            val awayImageId = awayTeamObj.optString("image_id")
            val awayImageUrl = if (!awayImageId.isNullOrEmpty()) "$imageUrl$awayImageId.png" else ""
            val awayTeam = Team(awayTeamId, awayTeamName, awayImageUrl)
            val score = resultsObj.optString("ss")
            val statsObj = resultsObj.optJSONObject("stats")
            var stats: Stats? = null
            if (statsObj != null) {
                val corners = jsonArrayToList(statsObj.optJSONArray("corners"))
                val goals = jsonArrayToList(statsObj.optJSONArray("goals"))
                val penalties = jsonArrayToList(statsObj.optJSONArray("penalties"))
                val redCards = jsonArrayToList(statsObj.optJSONArray("redcards"))
                val substitutions = jsonArrayToList(statsObj.optJSONArray("substitutions"))
                val yellowCards = jsonArrayToList(statsObj.optJSONArray("yellowcards"))
                stats = Stats(corners, goals, penalties, redCards, substitutions, yellowCards)
                Log.d("stats == ",stats.toString())
            }
           /* if (statsObj != null && statsObj.length() != 0) {
                val corners = statsObj.getJSONObject("corners").let {
                    val oneC = it.getString("0").toIntOrNull()
                    val twoC = it.getString("1").toIntOrNull()
                    if (oneC != null && twoC != null) Pair(oneC, twoC) else null
                }

                val goals = statsObj.optJSONObject("goals")?.let {
                    val firstGoal = it.getString("0").toIntOrNull()
                    val secondGoal = it.getString("1").toIntOrNull()
                    if (firstGoal != null && secondGoal != null) Pair(firstGoal, secondGoal) else null
                }

                val penalties = statsObj.optJSONObject("penalties")?.let {
                    val firstPenalty = it.getString("0").toIntOrNull()
                    val secondPenalty = it.getString("1").toIntOrNull()
                    if (firstPenalty != null && secondPenalty != null) Pair(firstPenalty, secondPenalty) else null
                }

                val redCards = statsObj.optJSONObject("redcards")?.let {
                    val firstRedCard = it.getString("0").toIntOrNull()
                    val secondRedCard = it.getString("1").toIntOrNull()
                    if (firstRedCard != null && secondRedCard != null) Pair(firstRedCard, secondRedCard) else null
                }

                val substitutions = statsObj.optJSONObject("substitutions")?.let {
                    val firstSubstitution = it.getString("0").toIntOrNull()
                    val secondSubstitution = it.getString("1").toIntOrNull()
                    if (firstSubstitution != null && secondSubstitution != null) Pair(firstSubstitution, secondSubstitution) else null
                }

                val yellowCards = statsObj.optJSONObject("yellowcards")?.let {
                    val firstYellowCard = it.getString("0").toIntOrNull()
                    val secondYellowCard = it.getString("1").toIntOrNull()
                    if (firstYellowCard != null && secondYellowCard != null) Pair(firstYellowCard, secondYellowCard) else null
                }

                stats = StatisticsModel(corners, goals, penalties, redCards, substitutions, yellowCards)
                Log.d("stats == ",stats.toString())
            }*/
            val eventsArray = resultsObj.optJSONArray("events")
            val events = mutableListOf<EventItemModel>()
            if (eventsArray != null) {
                for (i in 0 until eventsArray.length()) {
                    val eventObj = eventsArray.getJSONObject(i)
                    val eventId = eventObj.getString("id").toInt()
                    val eventText = eventObj.getString("text")
                    val eventItem = EventItemModel(eventId, eventText)
                    events.add(eventItem)
                }
            }
            val bet365IdStr = resultsObj.optString("bet365_id")
            val bet365Id = if (bet365IdStr.isNotBlank()) bet365IdStr.toInt() else 0

            return Event(
                eventId,
                homeTeam,
                awayTeam,
                score,
                timeStatus,
                Date(time),
                stats,
                events,
                bet365Id
            )
        } else {
            return null
        }
    }

    private fun jsonArrayToList(jsonArray: JSONArray?): List<String> {
        val list = mutableListOf<String>()
        if (jsonArray != null) {
            for (i in 0 until jsonArray.length()) {
                val value = jsonArray.optString(i)
                list.add(value)
            }
        }
        return list
    }

}