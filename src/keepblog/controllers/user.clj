(ns keepblog.controllers.user
  (:require [compojure.core :refer [defroutes GET POST]]
            [noir.session :as session]
            [noir.validation :as vali]
            [noir.response :as resp]
            [keepblog.views.user :as user-view]
            [keepblog.views.index :as index-view]
            [keepblog.models.user :as user]))

;; 登录
(defn login []
  (if (session/get :user-id)
    (resp/redirect "/")
    (user-view/login)))

;; 保存用户信息进 session
(defn save-user-info-in-session [{:keys [id username]}]
  (when (= username "admin")
    (session/put! :admin true)
    (println "Admin Login"))
  (session/put! :user-id id))

(defn new-session-action [user]
  (if (session/get :user-id)
    (resp/redirect "/")
    (user-view/login {:user user :error (first (vali/get-errors))})))
  
;; 登录 Action
(defn login-action [user]
  (if-let [user (user/login! user)]
    (do
      (save-user-info-in-session user)
      (resp/redirect "/"))
    (new-session-action user)))

;; 注销
(defn logout-action []
  (session/clear!)
  (resp/redirect "/"))

;; 注册
(defn register []
  (user-view/register))

;; 注册 Action
(defn register-action [new-user]
  (if-let [saved-user (user/create! new-user)]
    (do 
      (session/put! :user-id (:id saved-user))
      (resp/redirect "/"))
    (user-view/register {:user new-user :error (first (vali/get-errors))})))
      
;; 路由设置
(defroutes routes
  (GET "/login" [] (login))
  (POST "/login" {user :params} (login-action user))
  (GET "/logout" [] (logout-action))
  (GET "/register" [] (register))
  (POST "/register" {user :params} (register-action user)))