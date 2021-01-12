# Generator kart VCard pobierający informacje z zewnętrznej strony (panoramafirm)
## Api zwraca pliki vcard ze strony panoramafirm ze strony: `https://panoramafirm.pl/`
Podczas implementacji serwisu została wykorzystana zewnętrzna biblioteka jsoup, która umożliwia poberanie treści oraz przewtarzanie ich po stronie serwera. 

# Opis Api

<br />

Generowanie strony html z wyszukiwaniami
#### Adres URL: /vcard/?searchKey={searchKey}
#### Endpoint zwraca wygnereowaną strone html, która wyświetla listę kontaktów, które zostały pobrane ze strony https://panoramafirm.pl/ na bazie słowa kluczowego. 
### Metoda:GET 
####     Parametr:searchKey (String) - parametr wyszukiwania na stronie https://panoramafirm.pl/
Przykładowowe zapytanie:
`http://localhost:8080/search?searchKey=hydraulik`

<br />

Generowanie Vcard
#### Adres URL: /vcard/?companyName={companyName}&telephone={telephone}&email={email}&address={adress`
### Endpoint zwraca plik vCard(.vcf), który posiada informacje, które zostały podane w parametrach
### Metoda:GET 
####     Parametry: 
###### UWAGA! PARAMETRY MUSZĄ BYĆ ZAKODOWANE URL ENCODING
##### companyName (String) - nazwa firmy
##### email (String) - email firmy
##### address (String) - adres firmy
##### telephone (String) - numer kontaktowy

<br />

Przykładowowe zapytanie:
`http://localhost:8080/VCard?companyName=name&telephone=telephone&email=email&address=address`

Przykaładowy plik wygenerowany plik kontakt@primasortinstalacje.pl.vcf
`
BEGIN:VCARD
VERSION:4.0
ORG:Łukasz Noga
N:Łukasz Noga
TEL:536792675
ADR;TYPE=WORK,PREF:;;al. Dębowa 21 Niepołomice 32-005
EMAIL;TYPE=INTERNET:kontakt@primasortinstalacje.pl
END:VCARD
`
