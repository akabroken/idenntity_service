


/*---------------------------------------------------------------------------*\
 *																			 
 * InterSwitch Ltd.															 
 * Kenya																	 
 * 			
 *
 * File Name:		psp_insert_supplier.sql												  
 * Version Number:	1												 
 *
 * 
 *
 * Copyright (C) 2021                              							 
 * This software may not be copied or distributed in any form without the	 
 * written permission of InterSwitch Ltd.									 
 *																			 
\*---------------------------------------------------------------------------*/

/*=========================*/
/* 	MODIFICATION LOG 	   */
/*=========================*/
/*
 *	Ver.	Author			Date			Doc. Ref.	Summary of Changes
 *  1		Linus			07/08/2021      			Created
 *
 *
 *	Last modified in: v1.0.0
\*---------------------------------------------------------------------------*/
USE [tims]
GO

IF EXISTS (SELECT * FROM sysobjects WHERE id = object_id('psp_insert_supplier'))
BEGIN
    PRINT 'Dropping current psp_insert_supplier'
   	DROP PROCEDURE psp_insert_supplier
END
GO

CREATE PROCEDURE [dbo].[psp_insert_supplier]
(
	@name varchar(50),	
	@location varchar(100),
	@coordinates varchar(100),
	@pin varchar(50)	
)
AS

BEGIN
		DECLARE @serno bigint

		SET @serno =  NEXT VALUE FOR tims.suppliers_serno_seq
					
		INSERT INTO suppliers(serno,name,location,coordinates,pin)
			VALUES(@serno,@name,@location,@coordinates,@pin);	

		RETURN 0;
END		  
GO

PRINT 'Created: psp_insert_supplier'

--------------------------------------------------------------------------------
GRANT  EXECUTE  ON dbo.psp_insert_supplier TO tims_private
GO

-----------------------------------------------------------------------------------------------
