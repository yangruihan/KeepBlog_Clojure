(ns keepblog.models.article
  (:use [korma.db :refer [defdb mysql]]
        [korma.core :exclude [update]]
        [keepblog.models.korma_config])
  (:require [clojure.string :as string] 
            [noir.util.crypt :as crypt]
            [noir.validation :as vali]
            [keepblog.models.user :as user]))

;; 获得该用户所有文章信息
(defn get-all-articles-by-user [user]
  (if-let [user-id (:id (first (user/get-by user)))]
    (select articles
            (where {:user_id user-id})
            (order :create_time :DESC))
    (vali/set-error :base "获得所有文章失败"))) 

;; 根据条件获得相应的文章信息
(defn get-by [conditions]
  (select articles
          (where conditions)))

;; 创建文章
(defn- create [article]
  (try
    (insert articles
            (values article))
    (catch Exception e
      (println e)
      nil)))

;; 判断是否创建文章成功
(defn create! [article]
  (try
    (if (create article)
      (first (get-by article))
      (vali/set-error :create-article-error "文章创建失败"))
    (catch Exception e
      (println e)
      (vali/set-error :base "数据库插入失败")
      nil)))

;; 根据 ID 删除文章
(defn delete-by-id! [id]
  (delete articles
          (where {:id id})))