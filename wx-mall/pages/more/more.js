var util = require('../../utils/util.js');
var api = require('../../config/api.js');

let col1H = 0;
let col2H = 0;

Page({

  data: {
    scrollH: 0,
    imgWidth: 0,
    loadingCount: 0,
    images: [],
    col1: [],
    col2: []
  },

  onLoad: function (options) {
    let sceneId = options.sceneId;
    wx.getSystemInfo({
      success: (res) => {
        let ww = res.windowWidth;
        let wh = res.windowHeight;
        let imgWidth = ww * 0.48;
        let scrollH = wh;

        this.setData({
          scrollH: scrollH,
          imgWidth: imgWidth
        });

        this.loadImages(sceneId);
      }
    })
  },

  onImageLoad: function (e) {
    let imageId = e.currentTarget.id;
    let oImgW = e.detail.width;         //图片原始宽度
    let oImgH = e.detail.height;        //图片原始高度
    let imgWidth = this.data.imgWidth;  //图片设置的宽度
    let scale = imgWidth / oImgW;        //比例计算
    let imgHeight = oImgH * scale;      //自适应高度

    let images = this.data.images;
    let imageObj = null;

    for (let i = 0; i < images.length; i++) {
      let img = images[i];
      if (img.id === parseInt(imageId)) {
        imageObj = img;
        break;
      }
    }

    imageObj.height = imgHeight;

    let loadingCount = this.data.loadingCount - 1;
    let col1 = this.data.col1;
    let col2 = this.data.col2;

    if (col1H <= col2H) {
      col1H += imgHeight;
      col1.push(imageObj);
    } else {
      col2H += imgHeight;
      col2.push(imageObj);
    }

    let data = {
      loadingCount: loadingCount,
      col1: col1,
      col2: col2
    };

    if (!loadingCount) {
      data.images = [];
    }

    this.setData(data);
  },

  loadImages: function (sceneId) {
    let that = this;
    let images = [];
    util.request(api.SuggestItem, { scene: sceneId }).then(function (res) {
      if (res.code == 0) {
        that.setData({
          loadingCount: res.items.length,
          images: res.items
        });
        let baseId = "img-" + (+new Date());

        for (let i = 0; i < images.length; i++) {
          images[i].id = baseId + "-" + i;
        }
      }
    });
  },
  itemClick: function (e) {
    var itemType = e.currentTarget.dataset.type
    if (0 == itemType) {
      wx.navigateTo({
        url: '../goods/goods?id=' + e.currentTarget.dataset.goodsid
      });
    } else if (1 == itemType) {
      wx.navigateTo({
        url: '../topicDetail/topicDetail?id=' + e.currentTarget.dataset.goodsid
      });
    }
  }
})