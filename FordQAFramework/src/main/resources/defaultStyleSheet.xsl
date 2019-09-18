<xsl:transform version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method="html" doctype-system="http://www.w3.org/TR/html4/strict.dtd" doctype-public="-//W3C//DTD HTML 4.01//EN" indent="yes" /> 
<xsl:template match="/">
 <html>
 <head>
	<script src="http://code.jquery.com/jquery-latest.js"></script>
	
	<style>
	
	* {
		font-family: "Lucida Grande","Lucida Sans Unicode",sans-serif;
		font-size: 13px;
	}
	
	.master-table {
		display: none;
	}
	
	.header-row {
		position: relative;
	}
	
	.header-row span {
	}
	
	.toggle {
		display: block;
		float: left;
	}
	
	.header-row a,
	.header-row a:visited,
	.header-row a:active {
		margin: 1px 0;
		font-size: 1.2em;
		text-decoration: none;
		width: 16px;
		height: 16px;
		display: block;
	}
	
	.expand {
		background-image: url(data:image/jpeg;base64,/9j/4AAQSkZJRgABAQEASABIAAD/2wBDAAEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQH/2wBDAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQH/wAARCAARABADAREAAhEBAxEB/8QAGAAAAgMAAAAAAAAAAAAAAAAAAAQGCQr/xAAiEAACAwACAQUBAQAAAAAAAAAEBQMGBwECCAAUN3e2EhP/xAAUAQEAAAAAAAAAAAAAAAAAAAAA/8QAFBEBAAAAAAAAAAAAAAAAAAAAAP/aAAwDAQACEQMRAD8A1Y5lX/H11W8dT3Or5U51fQcqrt5J6WOr1lpb7YRIgVmWCxMj2C4g9m1ZnkMGU5TAmRm87h2FnD73qmekBAXiv+PvagboZmtXyoa85PVbd2JZVKr1lfZKPb1lVYOE54LNauHPWNVh4/8AYLdYR17r36hiBEXA7QsxgQaX41Xtk8b8SXsCSkFnQZrnjii3pPz2hsNLsMNURTDslpEMws8gsk4onLNZwWN1N6jDEDkr3C9Q3WBILRmVQyLxh1OlUpd7FUDlWiTEETc9JmbpnNTmfQx27M6Rw++anf4xcSy8RQjjjwjLlwwKoEAAYKQd2+b9k+1dD/XN/QGE/N+N/auefrlHoP/Z);		
	}
	
	.close {
		background-image: url(data:image/jpeg;base64,/9j/4AAQSkZJRgABAQEASABIAAD/2wBDAAEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQH/2wBDAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQH/wAARCAARABADAREAAhEBAxEB/8QAGQAAAQUAAAAAAAAAAAAAAAAAAAEFBgkK/8QAIBAAAgIDAAIDAQAAAAAAAAAABAUCBgEDBwAIN3e2Ev/EABQBAQAAAAAAAAAAAAAAAAAAAAD/xAAUEQEAAAAAAAAAAAAAAAAAAAAA/9oADAMBAAIRAxEAPwDU2IJ6+0v19ovXOuUXn7U9rz+mN3DhvTKy9t93t72shMyYxJZhSPsNqsJ8jDiyzjMz2Tye6dHjADMmQ4SC3VHhR/Crr1LltK5rpnp5rcLTTLnVqfX1LZS2U19oWtaqmQisNsisCJsHGUZRkC5ROQZatsQ2Qc4agdeWclpVtpvrV0WxAlNnlD5BTdNaDMYFEV4A46tVsnW+gg3bJrY2BdMPOAWmnTq3f1sHJNwYchqxSEFuXJaVyzi3slKkglKArjSuhWM1HBgVKvKjs0M4PbqriPM8LUYpE9Ey98Ax8bpy3al+N8UaiuqUwUl92+b+yfavQ/1zfwDhPzfxv7V55+uUeB//2Q==);
	}
	
	.child-row {
		display: none;
	}
	
	.test-fail {
		background-color: #e90707 !important;
		color: #f4e072;
	}
	
	.test-success {
		background-color: #6b9b00 !important;
		color: white;
	}
	
	.header-image-container {
		height: 50px;
	}
	</style>
	
	<!--[if IE 7]>
	<style>
	.header-row a,
	.header-row a:visited,
	.header-row a:active {
		position: absolute;
		left: 1px;
	}	
	</style>
	<![endif]-->
</head>
 <body>
 <br/>
  <xsl:for-each select="Log">	
	<table border="0" bordercolor="DarkGray" width="100%" bgcolor="WhiteSmoke">
		<tr>
            <td bgcolor="#FFFFFF" width="1"><img src="logo.png"/></td>
			<td align="center" bgcolor="#FFFFFF" height="30"><b>
                <p style="font-size:26px; color:#000">ENGInE Test Report</p>
            </b></td>
		</tr>
		<tr>
			<td colspan="3">
				<table border="0" width="90%" align="center">
					<tr bgcolor="#919191">
						<th width="15%">Test Run</th>
						<th width="15%">Status</th>
						<th width="15%">Started</th>
						<th width="15%">Ended</th>
						<th width="10%">Total Executed</th>
						<th width="10%">Pass</th>
						<th width="10%">Failed</th>
						<th width="10%">Blocked</th>
					</tr>
					<tr>
						<td><xsl:value-of select="@TestRunID"/></td>
						
						<xsl:choose>
							<xsl:when test="@Status='FAIL'">
								<td align="center"><font color="red"><xsl:value-of select="@Status"/></font></td>		
							</xsl:when>
							<xsl:when test="@Status='PASS'">
								<td align="center"><font color="green"><xsl:value-of select="@Status"/></font></td>		
							</xsl:when>
							<xsl:when test="@Status='ERROR'">
								<td align="center"><font color="red"><xsl:value-of select="@Status"/></font></td>		
							</xsl:when>
							<xsl:otherwise>
								<td align="center"><xsl:value-of select="@Status"/></td>		
							</xsl:otherwise>
						</xsl:choose>
						
						<td><xsl:value-of select="@Start"/></td>
						
						<td><xsl:value-of select="@Finish"/></td>

						<td><xsl:value-of select="@TotalNumberExecuted"/></td>

						<td><xsl:value-of select="@TotalPass"/></td>

						<td><xsl:value-of select="@TotalFail"/></td>

						<td><xsl:value-of select="@TotalBlocks"/></td>
					</tr>
				</table>		
			</td>
		</tr>
		<tr>
			<td colspan="3">
				<table border="1" width="100%" class="master-table">
				<xsl:for-each select="TestIteration">
					<tr>
						<td colspan="7" align="center" bgcolor="#919191">
							<b>Test Iteration: <font color="#FFFFFF"><xsl:number value ="position()"/></font>, TestName: <font color="#FFFFFF"><xsl:value-of select="@TestIdentifier"/></font>, Started: <font color="#FFFFFF"><xsl:value-of select="@Start"/></font>, Finished: <font color="#FFFFFF"><xsl:value-of select="@Finish"/></font>, Status =
								<xsl:choose>
									<xsl:when test="@Status='FAIL'">
										<font color="red">FAIL</font>
									</xsl:when>
									<xsl:when test="@Status='ERROR'">
										<font color="red">ERROR</font>
									</xsl:when>
									<xsl:when test="@Status='PASS'">
										<font color="green">PASS</font>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="@Status"/>
									</xsl:otherwise>
								</xsl:choose>						
							</b>															
						</td>
					</tr>
					<tr bgcolor="Silver">
						<th width="5%">#</th>
						<th width="10%">Time</th>
						<th width="5%">Status</th>
						<th width="10%">Step Name</th>
						<th width="20%">Expected</th>
						<th width="20%">Actual</th>
						<th width="35%">Details</th>
					</tr>
						<xsl:for-each select="Step">						
						 <tr> 
							<td align="center"><xsl:number value ="position()"/></td>
							<td><xsl:value-of select="Time"/></td>
							<td align="center">
								<xsl:choose>
									<xsl:when test="Status='FAIL'">
										<font color="red">FAIL</font>
									</xsl:when>
									<xsl:when test="Status='ERROR'">
										<font color="red">ERROR</font>
									</xsl:when>
									<xsl:when test="Status='PASS'">
										<font color="green">PASS</font>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="Status"/>
									</xsl:otherwise>
								</xsl:choose>							
							</td>
							<td><xsl:value-of select="StepName"/></td>
							<td><xsl:value-of select="Expected"/></td>
							<td><xsl:value-of select="Actual"/></td>
							<td><xsl:value-of select="Details"/></td>
						</tr>
					</xsl:for-each><!-- Step -->
				
				</xsl:for-each> <!-- Iteration -->				
				</table>
			</td>
		</tr>	
		
	</table>
	<br/><br/>
  </xsl:for-each> <!-- Log -->

<script type="text/javascript" >
$(document).ready( function() { 
	
	function fixLogo() {
		var image = $('table:first').find( 'tr:first img');
		if( image.length === 0 ) {
			return;
		}
		
		image.attr('src', 
			'logo.png');
		image.removeAttr( 'width' );
		image.removeAttr( 'height' );
		image.parents( 'td' ).removeAttr('height').addClass( 'header-image-container' );
	}
	
	//use a closure to create a namespace
	(function() {
		var td = null;	//identify the row context for each header row
		var id = 0;			//will identify the rel id for the header rows
		var table = $('table.master-table tbody');
		
		if( table.length == 0 ) {
			return; //table not found. do nothing
		}
		
		//scan the table and look for hook points
		$('> tr', table ).each( function() {
			//consider this tr only if it has only one child
			if( $(this).children().length > 1 ) {
			           if( !td ) {
			               return; //no header row context. go on.
			           }
			           //connect this child with the header row
			           $(this).addClass('child-row');
			           $(this).attr('rel', 'child_' + id );
			    return;
			}

			       id += 1;
			//let's consider this row for augmentation
			td = $($('> td', this).get(0));
			td.addClass('header-row');
			td.append( '&lt;a href="" class="toggle expand" rel="' + id + '"&gt;&lt;span&gt;&lt;/span&gt;&lt;/a&gt;' );
		});
		
		//now all the hooks are in place. plug-in the behavior
		$('.toggle' ).click( function(evt) {
			evt.preventDefault();
			$('tr[rel=child_' + ($(this).attr('rel')) + ']', table ).toggle();
			$(this).toggleClass( 'expand' );
			$(this).toggleClass( 'close' );
			return false;
		});
		
		//mark in colors success/fail
		$('.header-row' ).each( function() { 
		    var row=$(this);
		
				$('font', row ).each( function() { 
					var col = $(this).attr('color');
					row.addClass('color_' + col );
					
					if( col === 'red' || col === "#ff0000" ) {
						row.addClass( 'test-fail' );
						$(this).removeAttr('color');
					}
					else if( col === 'green' || col === "#008000" ) {
						row.addClass( 'test-success' );
						$(this).removeAttr('color');
					}
				});
				
				/*
		    if( $('font[color="red"]', row).length > 0 ) {
		        row.addClass( 'test-fail' );
		    }
		    if( $('font[color="green"]', row).length > 0 ) {
		        row.addClass( 'test-success' );
		    }
				//row.removeAttr('bgColor');
        $('font[color=red]', row ).removeAttr( 'color' );
				$('font[color=green]', row ).removeAttr( 'color' );
				*/
		} );
		
		//fix the problem with the main logo
		fixLogo();
				
		//finished. now show the table
		$('.master-table' ).fadeIn();
	}());
	
});
</script>
 </body>
 </html>


</xsl:template>
</xsl:transform>
