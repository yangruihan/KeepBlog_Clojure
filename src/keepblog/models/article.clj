(ns keepblog.models.article
  (:use [korma.db :refer [defdb mysql]]
        [korma.core :exclude [update]]
        [keepblog.models.korma_config])
  (:require [clojure.string :as string] 
            [noir.util.crypt :as crypt]
            [noir.validation :as vali]
            [keepblog.models.user :as user]))

(defn get-all-articles-by-user [user]
  (if-let [user-id (:id (first (user/get-by user)))]
    (select articles
            (where {:user_id user-id})
            (order :create_time :DESC))
    (vali/set-error :base "获得所有文章失败")))  