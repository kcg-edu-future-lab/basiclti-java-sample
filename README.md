# このリポジトリについて

https://github.com/IMSJapan/basiclti-java-sample を元に、以下の変更を行ったものです。

* spring-boot 2.5.4 へのバージョンアップ
* Java11 へのバージョンアップ
* basiclti-utilの 1.2.0 へのバージョンアップ
* 起動時のコンテキストパスの追加(/basiclti-java-sample)
* テンプレート(index.html, error.html)の修正
* リバースプロキシ下での動作を意識したX-Forwarded-Proto対応
コミットログ: https://github.com/kcg-edu-future-lab/basiclti-java-sample/commit/d489361c0708b2adcac94c4dc025151accf04ac6

Apacheをリバースプロキシとして利用する場合の設定例を以下に示します。

```
<Location /basiclti-java-sample>
  ProxyPass http://localhost:8090/basiclti-java-sample
  ProxyPassReverse http://localhost:8090/basiclti-java-sample
  ProxyPreserveHost On
  RequestHeader set X-Forwarded-Proto https
</Location>
```

これ以降の文章は、オリジナルのリポジトリと同じ内容です。


# basiclti-java-sample

IMS Global Learning Tools Interoperability 1.1を利用した、LTI Tool Providerのサンプルプログラムです。
Webアプリケーションフレームワークとして、Spring Bootを利用しています。

- [Learning Tools Interoperability | IMS Global Learning Consortium](https://www.imsglobal.org/activity/learning-tools-interoperability)
- [IMSGlobal/basiclti-util-java](https://github.com/IMSGlobal/basiclti-util-java)
- [Spring Boot](https://projects.spring.io/spring-boot/)


# プログラム内容
## LtiController.java
- LMSからのローンチに成功すれば"success"、失敗すれば"error"と表示します。LTI Launchリクエストで渡された各種パラメータをコンソール(標準出力)に出力します。

## LtiWebController.java
- Web画面にLTI Launchリクエストで渡された各種パラメータを表示します。

## 1. 認証情報の検証
LTI Tool Consumerから渡されたkeyを受け取り、対応するsecretを返す。
本来であれば、渡されたキーに対応するSecretをDBや認証サーバに問い合わせて取得することが想定されるが、ここではサンプルのため、固定文字列”secret”を返却。

MockKeyService.java
```java
@Override
public String getSecretForKey(String key) {
  return "secret";
}
```

## 2.LMSからパラメータを受け取る
引数HttpServletRequestでLTI Tool ConsumerからのLaunchリクエストを、LtiVerificationResultで上記1の認証情報の検証結果を受け取る。

LtiController.java
```java
@Lti
	@RequestMapping(value="/launch", method=RequestMethod.POST)
	public String ltiEntry(HttpServletRequest request, LtiVerificationResult result) {
    ...中略...
  }
```

# 実行方法
1. Windowsであればコマンドプロンプト，Macであればターミナルでサンプルプロジェクトの直下に移動
2. サンプルプログラムの実行パッケージを作成
```
$ mvn package
```
3. targetディレクトリの中に，サンプルプログラムの実行パッケージができている
```
$ ls ./target/
classes                    maven-archiver
demo-0.0.1-SNAPSHOT.jar            maven-status
demo-0.0.1-SNAPSHOT.jar.original    surefire-reports
generated-sources            test-classes
generated-test-sources
```
4. サンプルプログラム実行パッケージを実行し，学習ツール（Spring Bootサーバ）を起動
```
$ java -jar target/demo-0.0.1-SNAPSHOT.jar
```
5. ブラウザでSakaiからアクセス，動作を確認
 - LtiController: http://[host]/launch
 - LtiWebController: http://[host]/launchweb
6. (終了方法) Ctrl + C でサーバを終了
