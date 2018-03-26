

var contextPath = "/exam3";


// This will use a different driver order.
localforage.config({
    driver: [localforage.WEBSQL,
        localforage.INDEXEDDB,
        localforage.LOCALSTORAGE],
    name: 'RereExam'
});



// 创建一个模型用来支撑我们的购物视图
var examModule = angular.module('ExamModule', []);

function getDeferred() {
    var defer;
    try {
        var qq = angular.injector(['ExamModule', 'ng', 'ionic']).get('$q');
        defer = qq.defer();
    } catch (e) {
        //console.log(e);
        defer = $.Deferred();
    }
    return defer;
}




