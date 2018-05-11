<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<link rel="stylesheet" type="text/css" href="${staticPath }/static/style/css/icons-view.css?v=60" />
<script type="text/javascript" src="${staticPath }/static/clipboard/clipboard.min.js" charset="utf-8"></script>
<div class="container">
    <div class="interchange-body">
        <div id="clipIcon" class="large-12 columns">
            <h2 class="with-download-link">奖池总览</h2>
            <ul class="small-block-grid-2 large-block-grid-4">
                <li>玩家总数：${operating.playerAmount }人</li>
                <li>销售房卡：${operating.salesRoomcard }张</li>
                <li>已使用房卡：${operating.useRoomcard }张</li>
                <li>房卡利润：${operating.roomcardProfit }元</li>
                <li>房卡毛利润：${operating.roomcardNetProfit }元</li>
                
                <li>已投放保底金额：${jackpot.sendoutMinimum }元</li>
                <li>保底奖池：${jackpot.minimum }元</li>
                <li>领取条件总和：${jackpot.getNumber }个</li>
                <li>当前人均保底：${perCapita }元</li>
                
                <li>已投放众筹金额：${jackpot.sendoutCrowdfunding }元</li>
                <li>众筹奖池：${jackpot.crowdfunding }元</li>
                <li>已投资金额：${jackpot.investmentAmount }元</li>
                <li>每轮概率奖金：${jackpot.bonus }元</li>
                
                <li>总投放金币：${jackpot.sendoutGold }个</li>
                <li>总投放银币：${jackpot.sendoutSilver }个</li>
                
            </ul>
        </div>
    </div>
</div>
