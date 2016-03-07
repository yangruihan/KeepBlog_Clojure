(ns keepblog.web
  (:require [ring.adapter.jetty :as ring]
				    [ring.middleware.cookies :as cookies]
				    [ring.middleware.params :as params]
				    [ring.middleware.keyword-params :as keyword-params]
				    [ring.middleware.json :as json]
				    [ring.middleware.resource :as resource]
				    [ring.middleware.stacktrace :as stacktrace]
		        [keepblog.middleware.templating :as templating]
		        [keepblog.middleware.urlhandlers :as urlhandlers]
	          [keepblog.models.migration :as schema]))

(def app
  (-> urlhandlers/app-routes
    (resource/wrap-resource (clojure.java.io/resource "resources"))  ;; static resource
    templating/wrap-template-response  ;; render template
    json/wrap-json-response            ;; render json
    json/wrap-json-body                ;; request json
    stacktrace/wrap-stacktrace-web     ;; wrap-stacktrace-log
    keyword-params/wrap-keyword-params ;; convert parameter name to keyword
    cookies/wrap-cookies               ;; get/set cookies
    params/wrap-params                 ;; query string and url-encoded form
))

(defn start [port]
  (ring/run-jetty app {:port port,
                       :join? false}))

(defn -main []
  (schema/migrate)
  (let [port (Integer/parseInt (or (System/getenv "PORT") "3000"))]
    (start port)))

