// Ionic Starter App

// angular.module is a global place for creating, registering and retrieving Angular modules
// 'starter' is the name of this angular module example (also set in a <body> attribute in index.html)
// the 2nd parameter is an array of 'requires'

//保存远程系统地址
//101.201.145.254
//http://localhost:8094/exam3
var contextPath = "http://101.201.145.254";

var uid = "6a696b60-430d-4995-9ff6-8316aa2de6cc";//当前登录用户的ID
//var ifLogin = localStorage.getItem("rereexam.ifLogin");//是否已经登录,此信息应在clientSessionCtr中再封装一次
//var user = JSON.parse(localStorage.getItem("rereexam.user"));//记录当前登录的用户信息,此信息应在clientSessionCtr中再封装一次

// This will use a different driver order.
localforage.config({
    driver: [localforage.WEBSQL,
        localforage.INDEXEDDB,
        localforage.LOCALSTORAGE],
    name: 'RereExam'
});


//从本地保存中检查是否存储了系统地址，如果保存了，则加载存储的配置
var tempPath = localStorage.getItem("rereexam.sysPath");
if (tempPath !== null) {
    contextPath = tempPath;
}

var examModule = angular.module('ExamModule', ['ionic'])
        .run(function ($ionicPlatform) {
            $ionicPlatform.ready(function () {
                // Hide the accessory bar by default (remove this to show the accessory bar above the keyboard
                // for form inputs)
                if (window.cordova && window.cordova.plugins.Keyboard) {
                    cordova.plugins.Keyboard.hideKeyboardAccessoryBar(true);
                }
                if (window.StatusBar) {
                    StatusBar.styleDefault();
                }
            });
        });

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

examModule.config(['$sceDelegateProvider', function ($sceDelegateProvider) {
        var url = contextPath + "/**";
        $sceDelegateProvider.resourceUrlWhitelist(['self', url]);
    }]);


// 设置好服务工厂， 用来创建我们的 Items 接口， 以便访问服务端数据库
examModule.factory('CasusList', function ($rootScope, $http) {
    var items = {};//json对象
    items.query = function () {
        var exams = [];
        var str = localStorage.getItem("rereexam.casuses");
        //console.log("当前数据：" + str);
        if (str !== null) {
            exams = eval("(" + str + ")");
        }
        try {
            $http.get(contextPath + "/json/casus_list.jsp").success(function (data) {
                str = JSON.stringify(data);
                localStorage.setItem("rereexam.casuses", str);
                exams = eval("(" + str + ")");
                $rootScope.$broadcast("to-CasusList", str);//AJAX以异步方式调用，AJAX调用完成后，将数据通知到相关模型
            });
        } catch (e) {
            $rootScope.$broadcast("to-Reporter", e);//AJAX以异步方式调用，AJAX调用完成后，将数据通知到相关模型
        }
        //console.log("返回数据：" + exams);
        return exams;
    };
    return items;
});


//配置应用的路由规则
examModule.config(['$stateProvider', '$urlRouterProvider',
    function ($stateProvider, $urlRouterProvider) {


        $stateProvider.state('Menu', {
            url: "/event",
            abstract: true,
            templateUrl: "Menu.html"
        });
        $stateProvider.state('index', {
            cache: 'false',
            templateUrl: 'index.html'
        });
        $stateProvider.state('Default', {
            url: '/Default',
            cache: 'false',
            templateUrl: 'Default.html'
        });
        $stateProvider.state('MobileLogin', {
            cache: 'false',
            templateUrl: 'MobileLogin2.html'
        });
        $stateProvider.state('MobileRegister', {
            cache: 'false',
            templateUrl: 'MobileRegister.html'
        });
        $stateProvider.state('ExamLabelTypeList', {
            cache: 'false',
            templateUrl: 'ExamLabelTypeList.html'
        });
        $stateProvider.state('ExamList', {
            url: 'ExamList/:lid',
            cache: 'false',
            templateUrl: 'ExamList.html'
        });
        $stateProvider.state('MobileExamCase', {
            url: 'MobileExamCase/:eid',
            cache: 'false',
            templateUrl: 'MobileExamCase.html'
        });
        $stateProvider.state('ExamCaseList', {
            cache: 'false',
            templateUrl: 'ExamCaseList.html'
        });
        $stateProvider.state('ExamCaseResult', {
            url: 'ExamCaseResult/:data',
            cache: 'false',
            templateUrl: 'ExamCaseResult.html'
        });
        $stateProvider.state('ExamCaseReport', {
            url: 'ExamCaseReport/:eid',
            cache: 'false',
            templateUrl: 'ExamCaseReport.html'
        });
        $stateProvider.state('MobileUserDetail', {
            cache: 'false',
            templateUrl: 'MobileUserDetail.html'
        });
        $stateProvider.state('MobileCasusList', {
            cache: 'false',
            templateUrl: 'MobileCasusList.html'
        });
        $stateProvider.state('MobileCasusDetail', {
            url: 'MobileCasusDetail/:id',
            cache: 'false',
            templateUrl: 'MobileCasusDetail.html'
        });
        $stateProvider.state('QuestionCollectionType', {
            cache: 'false',
            templateUrl: 'QuestionCollectionType.html'
        });
        $stateProvider.state('QuestionCollectionList', {
            url: 'QuestionCollectionList/:qtype',
            cache: 'false',
            templateUrl: 'QuestionCollectionList.html'
        });
        $stateProvider.state('WrongQuestionType', {
            cache: 'false',
            templateUrl: 'WrongQuestionType.html'
        });
        $stateProvider.state('WrongQuestionList', {
            url: 'WrongQuestionList/:qtype',
            cache: 'false',
            templateUrl: 'WrongQuestionList.html'
        });

        $stateProvider.state('PreWrongExamCase', {
            cache: 'false',
            templateUrl: 'PreWrongExamCase.html'
        });
        $stateProvider.state('WrongExamCase', {
            url: 'WrongExamCase/:config2',
            cache: 'false',
            templateUrl: 'WrongExamCase.html'
        });

        $stateProvider.state('ContestList', {
            cache: 'false',
            templateUrl: 'ContestList.html'
        });
        $stateProvider.state('MobileContestCase', {
            url: 'MobileContestCase/:eid',
            cache: 'false',
            templateUrl: 'MobileContestCase.html'
        });
        $stateProvider.state('ContestCaseResult', {
            url: 'ContestCaseResult/:data',
            cache: 'false',
            templateUrl: 'ContestCaseResult.html'
        });
        $stateProvider.state('ContestCaseList', {
            url: 'ContestCaseList/:data',
            cache: 'false',
            templateUrl: 'ContestCaseList.html'
        });
        $stateProvider.state('ContestCaseReport', {
            url: 'ContestCaseReport/:eid',
            cache: 'false',
            templateUrl: 'ContestCaseReport.html'
        });

        $stateProvider.state('BuffetList', {
            cache: 'false',
            templateUrl: 'BuffetList.html'
        });
        $stateProvider.state('PreBuffetExamCase', {
            url: 'PreBuffetExamCase/:eid',
            cache: 'false',
            templateUrl: 'PreBuffetExamCase.html'
        });
        $stateProvider.state('MobileBuffetExamCase', {
            url: 'MobileBuffetExamCase/:data',
            cache: 'false',
            templateUrl: 'MobileBuffetExamCase.html'
        });
        $stateProvider.state('BuffetExamCaseResult', {
            url: 'BuffetExamCaseResult/:data',
            cache: 'false',
            templateUrl: 'BuffetExamCaseResult.html'
        });

        $stateProvider.state('LessonTypeList', {
            cache: 'false',
            templateUrl: 'LessonTypeList.html'
        });
        $stateProvider.state('LessonList', {
            url: 'LessonList/:tid',
            cache: 'false',
            templateUrl: 'LessonList.html'
        });
        $stateProvider.state('LessonDetail', {
            url: 'LessonDetail/:lid',
            cache: 'false',
            templateUrl: 'LessonDetail.html'
        });
        $stateProvider.state('BuyedLessonList', {
            cache: 'false',
            templateUrl: 'BuyedLessonList.html'
        });
        $stateProvider.state('DataCenter', {
            cache: 'false',
            templateUrl: 'DataCenter.html'
        });
        $stateProvider.state('Box', {
            cache: 'false',
            templateUrl: 'Box.html'
        });
        $stateProvider.state('Menu2', {
            cache: 'false',
            templateUrl: 'Menu2.html'
        });
        $stateProvider.state('BbsScore', {
            cache: 'false',
            templateUrl: 'BbsScore.html'
        });
        $stateProvider.state('Sms', {
            cache: 'false',
            templateUrl: 'Sms.html'
        });
        $stateProvider.state('UserCenter', {
            cache: 'false',
            templateUrl: 'UserCenter.html'
        });
        $stateProvider.state('Settings', {
            cache: 'false',
            templateUrl: 'Settings.html'
        });
        $stateProvider.state('Versions', {
            cache: 'false',
            templateUrl: 'Versions.html'
        });
        $stateProvider.state('AboutUs', {
            cache: 'false',
            templateUrl: 'AboutUs.html'
        });
        $stateProvider.state('UserScoreLogList', {
            cache: 'false',
            templateUrl: 'UserScoreLogList.html'
        });
        $stateProvider.state('KnowledgeLabelTypeList', {
            cache: 'false',
            templateUrl: 'KnowledgeLabelTypeList.html'
        });

        $urlRouterProvider.otherwise('Default');

    }
]);



examModule.controller("clientSessionCtr", function ($scope, $state, $ionicPopup, $http, $ionicSideMenuDelegate) {
    $scope.user = JSON.parse(localStorage.getItem("clientUser"));
    //console.log("user value:"+JSON.stringify($scope.user));
    $scope.question = {};
    $scope.qtype = "choice";

    var x = localStorage.getItem("rereexam.ifLogin");

    $scope.ifLogin = x==="true";//是否已经登录,此信息应在clientSessionCtr中再封装一次
    $scope.user = localStorage.getItem("rereexam.user");//记录当前登录的用户信息,此信息应在clientSessionCtr中再封装一次

    //console.log(x + ":"+$scope.ifLogin);


    $scope.exit = function () {
        localStorage.setItem("rereexam.ifLogin", false);
        $state.go("MobileLogin", {}, {reload: true});
    };

    $scope.gotoModule = function () {
        var mmpage = window.open(contextPath + "/mobile2/MobileExamModule2List2.jspx?uid=" + uid, '_blank', 'location=no');
        mmpage.addEventListener('loadstart', function (event) {
            var url = event.url;
            console.log(url);
            if (url.indexOf('ApkClose') !== -1) {
                mmpage.close();
            }
        });
    };
    $scope.gotoZone = function () {
        var zzpage = window.open(contextPath + "/mobile2/MobileZoneList.jspx?uid=" + uid, '_blank', 'location=no');
        zzpage.addEventListener('loadstart', function (event) {
            var url = event.url;
            console.log(url);
            if (url.indexOf('ApkClose') !== -1) {
                zzpage.close();
            }
        });
    };

    $scope.showQuestion = function (qtype, question) {
        $scope.question = question;
        $scope.qtype = qtype;
        var myPopup = $ionicPopup.show({
            templateUrl: 'ShowQuestion.html',
            title: '查看试题详情',
            scope: $scope,
            buttons: [{
                    text: '确定',
                    type: 'button-positive'
                }]
        });
        myPopup.then(function (res) {
            console.log('Tapped!', res);
        });
    };
    $scope.collectQuestion = function (qtype, qid) {
        var myPopup = $ionicPopup.show({
            template: '试题已收藏成功，请稍后到我的试题收藏中查看！',
            title: '收藏试题',
            scope: $scope,
            buttons: [{
                    text: '确定',
                    type: 'button-positive'
                }]
        });
        myPopup.then(function (res) {
            var uurl = contextPath + "/json/collect_question.jsp?uid=" + qid + "&qid=" + qid + "&qtype=" + qtype;
            $http.get(uurl).success(function (data) {
                console.log("收藏成功！");
            });
        });
    };
    $scope.reportWrong = function (qtype, qid) {
        var myPopup = $ionicPopup.show({
            template: '错题已向管理员报告成功！',
            title: '错题报告',
            scope: $scope,
            buttons: [{
                    text: '确定',
                    type: 'button-positive'
                }]
        });
        myPopup.then(function (res) {
            var uurl = contextPath + "/json/report_wrong.jsp?uid=" + qid + "&qid=" + qid + "&qtype=" + qtype;
            $http.get(uurl).success(function (data) {
                console.log("报错成功！");
            });
        });
    };

    $scope.toggleLeft = function () {
        $ionicSideMenuDelegate.toggleLeft();
    };

});






examModule.controller("mobileRegister", function ($scope, $state, $ionicPopup) {
    $scope.user = {}
    $scope.password2;
    $scope.register = function () {
        if ($scope.user.userName == null) {
            $ionicPopup.alert({
                title: '错误',
                template: '用户名不能为空！'
            });
            return false;
        }

        if ($scope.user.password == null) {
            $ionicPopup.alert({
                title: '错误',
                template: '密码不能为空！'
            });
            return false;
        }

        if ($scope.user.password !== $scope.password2) {
            $ionicPopup.alert({
                title: '错误',
                template: '您输入的密码与确认密码不一致！'
            });
            return false;
        }

        if ($scope.user.email == null) {
            $ionicPopup.alert({
                title: '错误',
                template: '邮箱不能为空！'
            });
            return false;
        }

        var str = JSON.stringify($scope.user);
        console.log(str);
        localStorage.setItem("clientUser", str);
        $state.go("Default", {}, {reload: true});
    };

    // A confirm dialog
    $scope.showConfirm = function () {
        var confirmPopup = $ionicPopup.confirm({
            title: 'Consume Ice Cream',
            template: 'Are you sure you want to eat this ice cream?'
        });
        confirmPopup.then(function (res) {
            if (res) {
                console.log('You are sure');
            } else {
                console.log('You are not sure');
            }
        });
    };
});






/**
 * 考试列表页面对应模型
 */
examModule.controller("mobileCasusList", function ($scope, CasusList) {
    $scope.casuses = CasusList.query();
    $scope.error = "";

    $scope.refresh = function () {
        $scope.casuses = CasusList.query();
        console.log("Refreshed");
    };

    //订阅数据更新通知
    $scope.$on("to-CasusList", function (event, msg) {
        //console.log("收到数据："+msg);
        str = eval("(" + msg + ")");
        $scope.casuses = str;
    });
    $scope.$on("to-Reporter", function (event, msg) {

        $scope.error = msg;
    });

});

/**
 * 考试页面
 */
examModule.controller("mobileCasusDetail", function ($scope, $http, $stateParams) {
    $scope.casus = {};
    $scope.loading = true;

    var id = $stateParams.id;
    //console.log("id:"+id);
    var uurl = contextPath + "/json/casus.jsp?id=" + id;
    //console.log(uurl);
    $http.get(uurl).success(function (data) {
        $scope.casus = data;
        $scope.loading = false;
        //var str = JSON.stringify(data);
        //console.log(str);
        //localStorage.setItem("id", str);
    });


});

examModule.controller("test", function ($scope, $state, $location) {
    var height = document.getElementById("wrapper1").clientHeight;
    console.log("height:" + height);
});
