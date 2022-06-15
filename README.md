# altitool

An Android app that provides coordinates and altitude in the Swiss system using GPS information.


This crate provides methods to convert Wgs84 coordinates into the Swiss coordinate formats LV03 (Landesvermessung 1903 or CH1903) and LV95 (Landesvermessung 1995 or CH1903+).

## Swiss coordinate system

See Wikipedia for more information:  
<https://en.wikipedia.org/wiki/Swiss_coordinate_system>

## Implementation

Based on the formulas described in the document "Näherungsformeln für die Transformation zwischen Schweizer Projektionskoordinaten und WGS84" by the "Bundesamt für Landestopografie swisstopo".
The formulas are only an estimation. Accuracy should be within 1 meter or 0.1'' respectively.

See this document for implementation details:  
<https://www.swisstopo.admin.ch/de/swisstopo/dokumente.detail.document.html/swisstopo-internet/de/documents/geo-documents/ch1903wgs84_d.pdf.html>
