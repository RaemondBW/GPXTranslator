This is a hacked together program to translate a proprietary/non-standard gps format. It is ugly code that lacks handled exceptions.

At this point is translates .gpx files to a custom class using sax parsing and then outputs to a csv.

The gpx only stores specific points of latitude and longitude with time. I interpolated a rough calculation of the speed, ignoring large changes in altitude and a heading.

A large bug left in the program is the handling of the times. I ignored the date. So gps data tracked that crosses midnight, would produce a strange output.

