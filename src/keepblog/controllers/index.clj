(ns keepblog.controllers.index
  (:require [compojure.core :refer [defroutes GET POST]]
            [noir.session :as session]
            [noir.validation :as vali]
            [noir.response :as resp]
            [keepblog.views.index :as index-view]
            [keepblog.models.user :as user]
            [keepblog.models.article :as article]))

;; 主页
(defn index []
  (if (session/get :user-id)
    (index-view/index 
      {:user (first (user/get-by {:id (session/get :user-id)})),
       :articles (article/get-all-articles-by-user {:id (session/get :user-id)})})
    (index-view/index)))

;; 路由设置
(defroutes routes
  (GET "/" [] (index)))
