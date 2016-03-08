(ns keepblog.models.category
  (:use [korma.db :refer [defdb mysql]]
        [korma.core :exclude [update]]
        [keepblog.models.korma_config])
  (:require [clojure.string :as string] 
            [noir.util.crypt :as crypt]
            [noir.validation :as vali]
            [keepblog.models.user :as user]))

;; 获得该用户所有分类信息
(defn get-all-categories-by-user [user]
  (if-let [user-id (:id (first (user/get-by user)))]
    (select categories
            (where {:user_id user-id}))
    (vali/set-error :base "获得所有分类失败")))

(defn get-category-by-article-id [article-id]
  (select categories
          (where {:id (:category_id (first (select articles
                                                   (where {:id article-id}))))})))
