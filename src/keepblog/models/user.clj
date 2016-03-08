(ns keepblog.models.user
  (:use [korma.db :refer [defdb mysql]]
        [korma.core :exclude [update]]
        [keepblog.models.korma_config])
  (:require [clojure.string :as string] 
            [noir.util.crypt :as crypt]
            [noir.validation :as vali]))

;; 向数据库中创建一条用户信息
(defn create [user]
  (let [{:keys [username password email]} user]
	  (try
     (insert users
	            (values {:username username,
	                     :password password,
	                     :email email}))
     (catch Exception e
       (println e)
       (vali/set-error :base-error "数据库插入失败")
       nil))))

;; 得到数据库中所有记录
(defn all []
  (select users))

;; 根据条件获得相应的用户信息
(defn get-by [conditions]
  (select users
          (where conditions)))

;; 判断登录是否成功
(defn login!
  [{:keys [username password]}]
  (let [{id :id,
         _password :password,
         :as user}
        (first (get-by {:username username}))]
    (if (nil? id)
      (vali/set-error :login-error "没有此账号")
      (if (= password _password)
        user
        (vali/set-error :login-error "密码错误")))))

;; 判断是否创建新用户成功
(defn create! [user]
  (try
	  (if (= 0 (count (get-by {:username (:username user)})))
	    (if (create user)
	      (first (get-by user))
	      (vali/set-error :create-user-error "创建用户发生未知错误"))
	    (vali/set-error :create-user-error "用户名已存在"))
   (catch Exception e
     (println e)
     (vali/set-error :base "数据库插入失败")
     nil)))
