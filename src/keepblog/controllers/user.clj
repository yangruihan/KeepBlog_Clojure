(ns keepblog.controllers.user
  (:require [compojure.core :refer [defroutes GET POST]]
            [noir.session :as session]
            [noir.validation :as vali]
            [noir.response :as resp]
            [keepblog.views.user :as user-view]
            [keepblog.views.index :as index-view]
            [keepblog.models.user :as user]))

;; 登录页面
(defn login []
  (user-view/login))

;; 保存用户信息进 session
(defn save-user-info-in-session [{:keys [id username]}]
  (when (= username "admin")
    (session/put! :admin true)
    (println "Admin Login"))
  (session/put! :user-id id))

(defn new-session-action [user]
  (if (session/get :user-id)
    (resp/redirect "/")
    (index-view/index {:error-msg (vali/get-errors)})))
  
;; 创建会话动作
(defn create-session-action [credentials]
  (if-let [user (user/login! credentials)]
    (do
      (save-user-info-in-session user)
      (index-view/index {:user user}))
    (new-session-action credentials)))
      
;; 路由设置
(defroutes routes
  (GET "/login" [] (login))
  (POST "/login" {user :params} (create-session-action user)))