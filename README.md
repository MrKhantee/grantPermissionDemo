# grantPermissionDemo
Android Grant Uri permission security
Access Contacts and Gallery without any permission.

Overview
Android 4.4 and below can access system content providers that has grant uri enabled.


Description
An app can give one-time access to data protected by a permission to an application component  using grant uri permission. A vulnerability exists where any app without the permission can provide access to system app's providers.

Technical details.

The android grant uri permission given through either Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION. ResolverActivity is a system app with access to all content providers. So if ResolverActivity is made to launch then the other launching app can get permission even though the launcher activity does not have permission.

The code for this is available at below location.


Impact
A number of system content providers use permission to protect data ( e.g contacts, gallery , mms data) but do provide grant uri permissions. All these content providers are vulnerable to this attack.

This vulnerability has already been addressed in Android 5.0 release.
