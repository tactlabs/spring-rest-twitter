package org.tact.base.service.impl;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.stereotype.Service;
import org.tact.base.domain.TwitterFeed;
import org.tact.base.service.TwitterFeedService;

@Service
public class SocialFeedServiceImpl implements TwitterFeedService {
	
	/**
	 * 
	 * 	
	 
	 	#twitter params
		twitter.consumerkey=
		twitter.consumersecret=
		twitter.accesstoken=
		twitter.accesstokensecret=
		# handle of the account which will favorite tweets for the site feed
		twitter.handle=
	  
	 * 
	 */
	
	private static Logger _log = LoggerFactory.getLogger(TwitterFeed.class);
	
	private static String TWITTER = "Twitter";
	
	private static String TWEET_TYPE = "TWEET";
	
	//@Value("${twitter.consumerkey}")
	private String twtConsumerKey = "";

	//@Value("${twitter.consumersecret}")
	private String twtConsumerSecret = "";

	//@Value("${twitter.accesstoken}")
	private String twtAccessToken = "";

	//@Value("${twitter.accesstokensecret}")
	private String twtAccessTokenSecret = "";

	//@Value("${twitter.handle}")
	private String twtHandle = "";

	@Override
	public List<TwitterFeed> getTwitterFeeds() {
		
		List<TwitterFeed> socialList = new LinkedList<TwitterFeed>();
		
		try {

			TwitterTemplate twitterTemplate = new TwitterTemplate(twtConsumerKey, twtConsumerSecret, twtAccessToken, twtAccessTokenSecret);

			//List<Tweet> tweets = twitterTemplate.timelineOperations().getUserTimeline(twtHandle);
			List<Tweet> tweets = twitterTemplate.timelineOperations().getFavorites(twtHandle, 30);

			for (Tweet tweet : tweets) {

				String strFeedId = tweet.getId() + "";

				TwitterFeed sf = new TwitterFeed();
				sf.setFeedId(strFeedId);
				sf.setSource(SocialFeedServiceImpl.TWITTER);
				sf.setLink("https://twitter.com/statuses/" + strFeedId);
				sf.setMessage(tweet.getText());
				sf.setSync_date(new Date());
				sf.setTitle("");
				sf.setType(SocialFeedServiceImpl.TWEET_TYPE);
				sf.setUploaded_date(tweet.getCreatedAt());
				sf.setUser_id(tweet.getFromUser());

				socialList.add(sf);
			}
			
			_log.debug("{insertTwitterFeeds} Completed insertTwitterFeeds");
			
			return socialList;

		} catch (Exception e) {
			e.printStackTrace();
			_log.warn("{insertTwitterFeeds}"+e.toString());
			
			return null;
		}		
	}
}
