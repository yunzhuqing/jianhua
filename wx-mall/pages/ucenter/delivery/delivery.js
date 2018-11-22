var util = require('../../../utils/util.js');
var api = require('../../../config/api.js');

// pages/ucenter/delivery/delivery.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    current: 2,
    verticalCurrent: 0,
    infos:[{"time":1, "desc":"dddd"}]
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    let orderId = options.orderId
    let that = this
    util.request(api.Logistics + '/' + orderId, null).then(function (res) {
      if (res.errno === 0) {
        that.setData({
          infos: res.data
        });
      }
    });
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {

  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  },
  handleClick() {
    const addCurrent = this.data.current + 1;
    const current = addCurrent > 2 ? 0 : addCurrent;
    this.setData({
      'current': current
    })
  }
})