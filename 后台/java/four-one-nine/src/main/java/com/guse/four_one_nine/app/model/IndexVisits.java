package com.guse.four_one_nine.app.model;

/**
 * @ClassName: IndexVisits
 * @Description: 首页统计实体
 * @author: wangkai
 * @date: 2018年2月5日 下午2:20:58
 * 
 */
public class IndexVisits {
	/**
	 * 故事接龙访问量（次数）
	 */
	private Long story_solitaire_visits;
	/**
	 * 交易访问量（次数）
	 */
	private Long transaction_visits;
	/**
	 * 涂鸦访问量（次数）
	 */
	private Long graffiti_visits;
	/**
	 * 日签访问量（次数）
	 */
	private Long day_sign_visits;
	/**
	 * 活动访问量（次数）
	 */
	private Long activities_visits;
	/**
	 * 海报访问量（次数）
	 */
	private Long posters_visits;

	public Long getStory_solitaire_visits() {
		return story_solitaire_visits;
	}

	public void setStory_solitaire_visits(Long story_solitaire_visits) {
		this.story_solitaire_visits = story_solitaire_visits;
	}

	public Long getTransaction_visits() {
		return transaction_visits;
	}

	public void setTransaction_visits(Long transaction_visits) {
		this.transaction_visits = transaction_visits;
	}

	public Long getGraffiti_visits() {
		return graffiti_visits;
	}

	public void setGraffiti_visits(Long graffiti_visits) {
		this.graffiti_visits = graffiti_visits;
	}

	public Long getDay_sign_visits() {
		return day_sign_visits;
	}

	public void setDay_sign_visits(Long day_sign_visits) {
		this.day_sign_visits = day_sign_visits;
	}

	public Long getActivities_visits() {
		return activities_visits;
	}

	public void setActivities_visits(Long activities_visits) {
		this.activities_visits = activities_visits;
	}

	public Long getPosters_visits() {
		return posters_visits;
	}

	public void setPosters_visits(Long posters_visits) {
		this.posters_visits = posters_visits;
	}

}
