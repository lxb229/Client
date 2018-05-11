<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<link rel="stylesheet" type="text/css" href="${staticPath }/static/style/css/icons-view.css?v=60" />
<script type="text/javascript" src="${staticPath }/static/clipboard/clipboard.min.js" charset="utf-8"></script>
<div class="container">
    <div class="interchange-body">
        <div id="clipIcon" class="large-12 columns">
            <h2 class="with-download-link">数据汇总</h2>
            <ul class="small-block-grid-2 large-block-grid-4">
                <li>注册总数：${dataMap.allPlayer }</li>
                <li>微信注册：${dataMap.wxPlayer }</li>
                <li>其他注册：${dataMap.otherPlayer }</li>
                <li>二日登录：${dataMap.twoDays }</li>
                <li>次日留存：${dataMap.twoDayRate }%</li>
                <li>三日登录：${dataMap.threeDays }</li>
                <li>三日留存：${dataMap.threeDayRate }%</li>
                <li>七日登录：${dataMap.sevenDays }</li>
                <li>七日留存：${dataMap.sevenDayRate }%</li>
                
                <li>最高峰值：${peakMap.maxPeak }</li>
                <li>最高峰值时间：${peakMap.maxTime }</li>
                <li>日均峰值：${peakMap.meanPeak }</li>
                <li>昨日峰值：${peakMap.yesterdayPeak }</li>
                <li>昨日峰值时间：${peakMap.yesterdayTime }</li>
                <li>目前在线：${peakMap.onlineNum }</li>
                
                <li>创建总数：${roomMap.allRooms }</li>
                <li>当日创建总数：${roomMap.todayRooms }</li>
                
                <li>加入总数：${joinRoomMap.allJoinRooms }</li>
                <li>当日加入总数：${joinRoomMap.todayJoinRooms }</li>
                
                <li>入局总数：${joinPartyMap.allJoinPartys }</li>
                <li>当日入局总数：${joinPartyMap.todayJoinPartys }</li>
                <li>总入局率：${joinPartyMap.partyRate }%</li>
                
                <li>牌局总数：${allPartysMap.allPartys }</li>
                <li>当日牌局总数：${todayPartysMap.todayParty }</li>
                
                <li>流水总数：${betMap.allBets }</li>
                <li>当日流水总数：${betMap.todayBets }</li>
                
                <li>筹码总数：${jettonMap.allJettons }</li>
                <li>当日筹码总数：${jettonMap.todayJettons }</li>
            </ul>
        </div>
    </div>
</div>
