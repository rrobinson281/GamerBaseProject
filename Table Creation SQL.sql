USE [master]
GO
/****** Object:  Database [GamerBase-S1G8-DEMO]    Script Date: 2/19/2021 1:12:54 AM ******/
CREATE DATABASE [GamerBase-S1G8-DEMO]
 CONTAINMENT = NONE
 ON  PRIMARY 
( NAME = N'GamerData', FILENAME = N'D:\Database\MSSQL15.MSSQLSERVER\MSSQL\DATA\GamerBaseDemo.mdf' , SIZE = 10240KB , MAXSIZE = 25600KB , FILEGROWTH = 10%)
 LOG ON 
( NAME = N'GamerLog', FILENAME = N'D:\Database\MSSQL15.MSSQLSERVER\MSSQL\DATA\GamerBaseDemoLog.ldf' , SIZE = 5120KB , MAXSIZE = 25600KB , FILEGROWTH = 10%)
 WITH CATALOG_COLLATION = DATABASE_DEFAULT
GO
ALTER DATABASE [GamerBase-S1G8-DEMO] SET COMPATIBILITY_LEVEL = 150
GO
IF (1 = FULLTEXTSERVICEPROPERTY('IsFullTextInstalled'))
begin
EXEC [GamerBase-S1G8-DEMO].[dbo].[sp_fulltext_database] @action = 'enable'
end
GO
ALTER DATABASE [GamerBase-S1G8-DEMO] SET ANSI_NULL_DEFAULT OFF 
GO
ALTER DATABASE [GamerBase-S1G8-DEMO] SET ANSI_NULLS OFF 
GO
ALTER DATABASE [GamerBase-S1G8-DEMO] SET ANSI_PADDING OFF 
GO
ALTER DATABASE [GamerBase-S1G8-DEMO] SET ANSI_WARNINGS OFF 
GO
ALTER DATABASE [GamerBase-S1G8-DEMO] SET ARITHABORT OFF 
GO
ALTER DATABASE [GamerBase-S1G8-DEMO] SET AUTO_CLOSE OFF 
GO
ALTER DATABASE [GamerBase-S1G8-DEMO] SET AUTO_SHRINK OFF 
GO
ALTER DATABASE [GamerBase-S1G8-DEMO] SET AUTO_UPDATE_STATISTICS ON 
GO
ALTER DATABASE [GamerBase-S1G8-DEMO] SET CURSOR_CLOSE_ON_COMMIT OFF 
GO
ALTER DATABASE [GamerBase-S1G8-DEMO] SET CURSOR_DEFAULT  GLOBAL 
GO
ALTER DATABASE [GamerBase-S1G8-DEMO] SET CONCAT_NULL_YIELDS_NULL OFF 
GO
ALTER DATABASE [GamerBase-S1G8-DEMO] SET NUMERIC_ROUNDABORT OFF 
GO
ALTER DATABASE [GamerBase-S1G8-DEMO] SET QUOTED_IDENTIFIER OFF 
GO
ALTER DATABASE [GamerBase-S1G8-DEMO] SET RECURSIVE_TRIGGERS OFF 
GO
ALTER DATABASE [GamerBase-S1G8-DEMO] SET  ENABLE_BROKER 
GO
ALTER DATABASE [GamerBase-S1G8-DEMO] SET AUTO_UPDATE_STATISTICS_ASYNC OFF 
GO
ALTER DATABASE [GamerBase-S1G8-DEMO] SET DATE_CORRELATION_OPTIMIZATION OFF 
GO
ALTER DATABASE [GamerBase-S1G8-DEMO] SET TRUSTWORTHY OFF 
GO
ALTER DATABASE [GamerBase-S1G8-DEMO] SET ALLOW_SNAPSHOT_ISOLATION OFF 
GO
ALTER DATABASE [GamerBase-S1G8-DEMO] SET PARAMETERIZATION SIMPLE 
GO
ALTER DATABASE [GamerBase-S1G8-DEMO] SET READ_COMMITTED_SNAPSHOT OFF 
GO
ALTER DATABASE [GamerBase-S1G8-DEMO] SET HONOR_BROKER_PRIORITY OFF 
GO
ALTER DATABASE [GamerBase-S1G8-DEMO] SET RECOVERY FULL 
GO
ALTER DATABASE [GamerBase-S1G8-DEMO] SET  MULTI_USER 
GO
ALTER DATABASE [GamerBase-S1G8-DEMO] SET PAGE_VERIFY CHECKSUM  
GO
ALTER DATABASE [GamerBase-S1G8-DEMO] SET DB_CHAINING OFF 
GO
ALTER DATABASE [GamerBase-S1G8-DEMO] SET FILESTREAM( NON_TRANSACTED_ACCESS = OFF ) 
GO
ALTER DATABASE [GamerBase-S1G8-DEMO] SET TARGET_RECOVERY_TIME = 60 SECONDS 
GO
ALTER DATABASE [GamerBase-S1G8-DEMO] SET DELAYED_DURABILITY = DISABLED 
GO
ALTER DATABASE [GamerBase-S1G8-DEMO] SET ACCELERATED_DATABASE_RECOVERY = OFF  
GO
EXEC sys.sp_db_vardecimal_storage_format N'GamerBase-S1G8-DEMO', N'ON'
GO
ALTER DATABASE [GamerBase-S1G8-DEMO] SET QUERY_STORE = OFF
GO
USE [GamerBase-S1G8-DEMO]
GO
/****** Object:  User [robinsr2]    Script Date: 2/19/2021 1:12:55 AM ******/
CREATE USER [robinsr2] FOR LOGIN [robinsr2] WITH DEFAULT_SCHEMA=[dbo]
GO
/****** Object:  User [fryaj1]    Script Date: 2/19/2021 1:12:55 AM ******/
CREATE USER [fryaj1] FOR LOGIN [fryaj1] WITH DEFAULT_SCHEMA=[dbo]
GO
ALTER ROLE [db_owner] ADD MEMBER [robinsr2]
GO
ALTER ROLE [db_owner] ADD MEMBER [fryaj1]
GO
/****** Object:  UserDefinedFunction [dbo].[fn_GetConsoleID]    Script Date: 2/19/2021 1:12:55 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
create function [dbo].[fn_GetConsoleID](@consoleName varchar(100))
returns int
begin
return (select ID as ID
from Console
where Name = @consoleName)
end
GO
/****** Object:  UserDefinedFunction [dbo].[fn_GetGameID]    Script Date: 2/19/2021 1:12:55 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
create function [dbo].[fn_GetGameID](@gameName varchar(100))
returns int
begin
return (select ID as ID
from Game
where Name = @gameName)
end
GO
/****** Object:  UserDefinedFunction [dbo].[fn_GetListID]    Script Date: 2/19/2021 1:12:55 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE function [dbo].[fn_GetListID](@listName varchar(100))
returns int
begin
return (select ID as ID
from list
where Name = @listname)
end
GO
/****** Object:  Table [dbo].[list]    Script Date: 2/19/2021 1:12:55 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[list](
	[ID] [int] IDENTITY(0,1) NOT NULL,
	[Name] [varchar](300) NOT NULL,
	[OwnerUsername] [varchar](15) NULL,
 CONSTRAINT [PK__list__3214EC27BF769EB6] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[GameAppearsOn]    Script Date: 2/19/2021 1:12:55 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[GameAppearsOn](
	[GameID] [int] NOT NULL,
	[ListID] [int] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[GameID] ASC,
	[ListID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  UserDefinedFunction [dbo].[fn_GetGameLists]    Script Date: 2/19/2021 1:12:55 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
create function [dbo].[fn_GetGameLists]()
returns table
as 
return (
	select distinct l.ID as ListID, l.Name as ListName
	from GameAppearsOn g join list l on g.ListID = l.ID
)
GO
/****** Object:  Table [dbo].[ConsoleAppearsOn]    Script Date: 2/19/2021 1:12:55 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[ConsoleAppearsOn](
	[ConsoleID] [int] NOT NULL,
	[ListID] [int] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[ListID] ASC,
	[ConsoleID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  UserDefinedFunction [dbo].[fn_GetConsoleLists]    Script Date: 2/19/2021 1:12:55 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE function [dbo].[fn_GetConsoleLists]()
returns table
as 
return (
	select distinct l.ID as ListID, l.Name as ListName
	from ConsoleAppearsOn c join list l on c.ListID = l.ID
)
GO
/****** Object:  Table [dbo].[Console]    Script Date: 2/19/2021 1:12:55 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Console](
	[ID] [int] IDENTITY(0,1) NOT NULL,
	[Name] [varchar](20) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  UserDefinedFunction [dbo].[fn_ViewConsoleLists]    Script Date: 2/19/2021 1:12:55 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE function [dbo].[fn_ViewConsoleLists](@listID int)
returns table
as 
return (
	select Con.Name as ConsoleName
	from ConsoleAppearsOn c join Console con on c.ConsoleID = con.ID
	where c.ListID = @listID
)
GO
/****** Object:  Table [dbo].[Game]    Script Date: 2/19/2021 1:12:55 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Game](
	[ID] [int] IDENTITY(0,1) NOT NULL,
	[Name] [varchar](50) NOT NULL,
	[Description] [varchar](500) NULL,
	[Genre] [varchar](30) NULL,
	[ReleaseDate] [date] NULL,
	[PublisherID] [int] NULL,
 CONSTRAINT [PK__Game__3214EC27F738B4D8] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  UserDefinedFunction [dbo].[fn_ViewGameLists]    Script Date: 2/19/2021 1:12:55 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE function [dbo].[fn_ViewGameLists](@listID int)
returns table
as 
return (
	select game.Name as GameName
	from GameAppearsOn g join Game game on g.GameID = game.ID
	where g.ListID = @listID
)
GO
/****** Object:  Table [dbo].[User]    Script Date: 2/19/2021 1:12:55 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[User](
	[Username] [varchar](15) NOT NULL,
	[PasswordSalt] [varchar](50) NULL,
	[PasswordHash] [varchar](50) NULL,
PRIMARY KEY CLUSTERED 
(
	[Username] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  UserDefinedFunction [dbo].[fn_UserGameLists]    Script Date: 2/19/2021 1:12:55 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE function [dbo].[fn_UserGameLists](@user varchar(15))
returns table
as 
return (
	select distinct l.ID as ListId, l.Name as ListName, l.OwnerUsername as Owner 
	from [User] u join list l on u.Username = l.OwnerUsername join GameAppearsOn g on g.ListID = l.ID
	where u.Username = @user
)
GO
/****** Object:  UserDefinedFunction [dbo].[fn_UserConsoleLists]    Script Date: 2/19/2021 1:12:55 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE function [dbo].[fn_UserConsoleLists](@user varchar(15))
returns table
as 
return (
	select distinct l.ID as ListId, l.Name as ListName, l.OwnerUsername as Owner 
	from [User] u join list l on u.Username = l.OwnerUsername join ConsoleAppearsOn c on c.ListID = l.ID
	where u.Username = @user
)
GO
/****** Object:  Table [dbo].[Profile]    Script Date: 2/19/2021 1:12:55 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Profile](
	[ID] [int] IDENTITY(0,1) NOT NULL,
	[ProfileType] [varchar](30) NULL,
	[ProfileName] [varchar](30) NOT NULL,
	[OwnerUsername] [varchar](15) NULL,
PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  UserDefinedFunction [dbo].[fn_FetchProfileID]    Script Date: 2/19/2021 1:12:55 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE function [dbo].[fn_FetchProfileID](@user varchar(15), @profileType varchar(30), @profileName varchar(30))
returns table
as 
return (
	select p.ID
	from Profile p
	WHERE p.ProfileName = @profileName and p.ProfileType = @profileType and @user = p.OwnerUsername
)
GO
/****** Object:  Table [dbo].[Rates]    Script Date: 2/19/2021 1:12:55 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Rates](
	[GameID] [int] NOT NULL,
	[RatingUser] [varchar](15) NOT NULL,
	[Review] [varchar](200) NULL,
	[Rating] [float] NULL,
PRIMARY KEY CLUSTERED 
(
	[GameID] ASC,
	[RatingUser] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  View [dbo].[ReadRatings]    Script Date: 2/19/2021 1:12:55 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE view [dbo].[ReadRatings]
as
select RatingUser, Name as GameName, Review, Rating
from Rates r join Game g on r.GameID = g.ID
GO
/****** Object:  UserDefinedFunction [dbo].[fn_UserReviews]    Script Date: 2/19/2021 1:12:55 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
create function [dbo].[fn_UserReviews](@user varchar(15))
returns table
as 
return (
	select Name as GameName, Review, Rating
	from Rates r join [User] u on r.RatingUser = u.Username join Game g on r.GameID = g.ID
	where u.Username = @user
)
GO
/****** Object:  Table [dbo].[PlayedOn]    Script Date: 2/19/2021 1:12:55 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[PlayedOn](
	[GameID] [int] NOT NULL,
	[ConsoleID] [int] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[GameID] ASC,
	[ConsoleID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  UserDefinedFunction [dbo].[fn_PlayableOnConsole]    Script Date: 2/19/2021 1:12:55 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
create function [dbo].[fn_PlayableOnConsole](@gameID int)
returns table
as
return (
	select c.Name as Console
	from Game g join PlayedOn p on g.ID = p.GameID
	join Console c on p.ConsoleID = c.ID
	where g.ID = @gameID
)
GO
/****** Object:  UserDefinedFunction [dbo].[fn_GamesWithRatingAbove]    Script Date: 2/19/2021 1:12:55 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE function [dbo].[fn_GamesWithRatingAbove](@rating float)
returns table 
as
Return (
	Select g.Name, g.Description, g.Genre, r.Rating
	from Game g join Rates r on g.ID = r.GameID
	where r.Rating >= @rating
)
GO
/****** Object:  UserDefinedFunction [dbo].[fn_UserProfiles]    Script Date: 2/19/2021 1:12:55 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE function [dbo].[fn_UserProfiles](@user varchar(15))
returns table
as 
return (
	select p.OwnerUsername, p.ProfileType, p.ProfileName
	from [User] u join Profile p on u.Username = p.OwnerUsername
	where u.Username = @user
)
GO
/****** Object:  UserDefinedFunction [dbo].[fn_GamesOnConsole]    Script Date: 2/19/2021 1:12:55 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
create function [dbo].[fn_GamesOnConsole](@console int)
returns table
as
return (
	select c.Name as Console, g.Name as Game, g.Genre
	from Game g join PlayedOn p on g.ID = p.GameID
	join Console c on p.ConsoleID = c.ID
	where c.ID = @console
)
GO
/****** Object:  UserDefinedFunction [dbo].[fn_GamesWithRatingBelow]    Script Date: 2/19/2021 1:12:55 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
create function [dbo].[fn_GamesWithRatingBelow](@rating float)
returns table 
as
Return (
	Select g.Name, g.Description, g.Genre, r.Rating
	from Game g join Rates r on g.ID = r.GameID
	where r.Rating <= @rating
)
GO
/****** Object:  Table [dbo].[FriendsWith]    Script Date: 2/19/2021 1:12:55 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[FriendsWith](
	[Friendee] [varchar](15) NOT NULL,
	[Friender] [varchar](15) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[Friendee] ASC,
	[Friender] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  UserDefinedFunction [dbo].[fn_ViewFriendsList]    Script Date: 2/19/2021 1:12:55 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE FUNCTION [dbo].[fn_ViewFriendsList]
	(@Username nvarchar(20) = NULL)
	RETURNS table
	AS
	Return (SELECT Friendee 
	FROM FriendsWith
	WHERE Friender = @Username)
GO
/****** Object:  UserDefinedFunction [dbo].[fn_AvailableGamesOnConsole]    Script Date: 2/19/2021 1:12:55 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE Function [dbo].[fn_AvailableGamesOnConsole](@consoleID int)
Returns Table
As
Return (Select Game.ID, Game.Name
		From Game Join PlayedOn on Game.ID = PlayedOn.GameID
		Where PlayedOn.ConsoleID = @consoleID)
GO
/****** Object:  Table [dbo].[Publisher]    Script Date: 2/19/2021 1:12:55 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Publisher](
	[ID] [int] IDENTITY(0,1) NOT NULL,
	[Name] [varchar](25) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  UserDefinedFunction [dbo].[fn_SortGames]    Script Date: 2/19/2021 1:12:55 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE FUNCTION [dbo].[fn_SortGames]
	(@GameName nvarchar(64) = null,
	@Publisher nvarchar(64) = null,
	@Genre nvarchar(32) = null,
	@ReleaseDate Date = null)
	RETURNS table
	AS
	Return (SELECT Game.Name as name, Description, Genre, Publisher.Name as Publisher,ReleaseDate
	FROM Game join Publisher on Game.PublisherID = Publisher.ID
	WHERE Game.Name = @GameName OR Genre = @Genre OR ReleaseDate = @ReleaseDate OR PublisherID = (SELECT ID FROM Publisher WHERE Name = @Publisher))
GO
/****** Object:  Table [dbo].[OwnsGame]    Script Date: 2/19/2021 1:12:55 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[OwnsGame](
	[GameID] [int] NOT NULL,
	[OwningUser] [varchar](15) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[GameID] ASC,
	[OwningUser] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  UserDefinedFunction [dbo].[fn_viewOwnedGames]    Script Date: 2/19/2021 1:12:55 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
create FUNCTION [dbo].[fn_viewOwnedGames]
	(@user nvarchar(64))
	RETURNS table
	AS
	Return (Select *
		from OwnsGame og join Game g on og.GameID = g.ID
		where og.OwningUser = @user)
GO
SET ANSI_PADDING ON
GO
/****** Object:  Index [ConsoleNameIndex]    Script Date: 2/19/2021 1:12:55 AM ******/
CREATE NONCLUSTERED INDEX [ConsoleNameIndex] ON [dbo].[Console]
(
	[Name] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
SET ANSI_PADDING ON
GO
/****** Object:  Index [GameNameIndex]    Script Date: 2/19/2021 1:12:55 AM ******/
CREATE NONCLUSTERED INDEX [GameNameIndex] ON [dbo].[Game]
(
	[Name] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
SET ANSI_PADDING ON
GO
/****** Object:  Index [ListNameIndex]    Script Date: 2/19/2021 1:12:55 AM ******/
CREATE NONCLUSTERED INDEX [ListNameIndex] ON [dbo].[list]
(
	[Name] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
ALTER TABLE [dbo].[ConsoleAppearsOn]  WITH CHECK ADD  CONSTRAINT [FK__ConsoleAp__Conso__44FF419A] FOREIGN KEY([ConsoleID])
REFERENCES [dbo].[Console] ([ID])
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[ConsoleAppearsOn] CHECK CONSTRAINT [FK__ConsoleAp__Conso__44FF419A]
GO
ALTER TABLE [dbo].[ConsoleAppearsOn]  WITH CHECK ADD  CONSTRAINT [FK__ConsoleAp__ListI__45F365D3] FOREIGN KEY([ListID])
REFERENCES [dbo].[list] ([ID])
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[ConsoleAppearsOn] CHECK CONSTRAINT [FK__ConsoleAp__ListI__45F365D3]
GO
ALTER TABLE [dbo].[FriendsWith]  WITH CHECK ADD FOREIGN KEY([Friendee])
REFERENCES [dbo].[User] ([Username])
GO
ALTER TABLE [dbo].[FriendsWith]  WITH CHECK ADD FOREIGN KEY([Friender])
REFERENCES [dbo].[User] ([Username])
GO
ALTER TABLE [dbo].[Game]  WITH CHECK ADD  CONSTRAINT [FK__Game__PublisherI__286302EC] FOREIGN KEY([PublisherID])
REFERENCES [dbo].[Publisher] ([ID])
GO
ALTER TABLE [dbo].[Game] CHECK CONSTRAINT [FK__Game__PublisherI__286302EC]
GO
ALTER TABLE [dbo].[GameAppearsOn]  WITH NOCHECK ADD  CONSTRAINT [FK__GameAppea__GameI__412EB0B6] FOREIGN KEY([GameID])
REFERENCES [dbo].[Game] ([ID])
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[GameAppearsOn] NOCHECK CONSTRAINT [FK__GameAppea__GameI__412EB0B6]
GO
ALTER TABLE [dbo].[GameAppearsOn]  WITH CHECK ADD  CONSTRAINT [FK__GameAppea__ListI__4222D4EF] FOREIGN KEY([ListID])
REFERENCES [dbo].[list] ([ID])
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[GameAppearsOn] CHECK CONSTRAINT [FK__GameAppea__ListI__4222D4EF]
GO
ALTER TABLE [dbo].[list]  WITH CHECK ADD  CONSTRAINT [FK__list__OwnerUsern__2B3F6F97] FOREIGN KEY([OwnerUsername])
REFERENCES [dbo].[User] ([Username])
GO
ALTER TABLE [dbo].[list] CHECK CONSTRAINT [FK__list__OwnerUsern__2B3F6F97]
GO
ALTER TABLE [dbo].[OwnsGame]  WITH CHECK ADD  CONSTRAINT [FK__OwnsGame__GameID__398D8EEE] FOREIGN KEY([GameID])
REFERENCES [dbo].[Game] ([ID])
GO
ALTER TABLE [dbo].[OwnsGame] CHECK CONSTRAINT [FK__OwnsGame__GameID__398D8EEE]
GO
ALTER TABLE [dbo].[OwnsGame]  WITH CHECK ADD FOREIGN KEY([OwningUser])
REFERENCES [dbo].[User] ([Username])
GO
ALTER TABLE [dbo].[PlayedOn]  WITH CHECK ADD FOREIGN KEY([ConsoleID])
REFERENCES [dbo].[Console] ([ID])
GO
ALTER TABLE [dbo].[PlayedOn]  WITH CHECK ADD  CONSTRAINT [FK__PlayedOn__GameID__3D5E1FD2] FOREIGN KEY([GameID])
REFERENCES [dbo].[Game] ([ID])
GO
ALTER TABLE [dbo].[PlayedOn] CHECK CONSTRAINT [FK__PlayedOn__GameID__3D5E1FD2]
GO
ALTER TABLE [dbo].[Profile]  WITH CHECK ADD FOREIGN KEY([OwnerUsername])
REFERENCES [dbo].[User] ([Username])
GO
ALTER TABLE [dbo].[Rates]  WITH CHECK ADD  CONSTRAINT [FK__Rates__GameID__35BCFE0A] FOREIGN KEY([GameID])
REFERENCES [dbo].[Game] ([ID])
GO
ALTER TABLE [dbo].[Rates] CHECK CONSTRAINT [FK__Rates__GameID__35BCFE0A]
GO
ALTER TABLE [dbo].[Rates]  WITH CHECK ADD FOREIGN KEY([RatingUser])
REFERENCES [dbo].[User] ([Username])
GO
/****** Object:  StoredProcedure [dbo].[AddConsoleToList]    Script Date: 2/19/2021 1:12:55 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE procedure [dbo].[AddConsoleToList] 
	@consoleID int,
	@listID int
as
if(not exists(select * from Console where ID = @consoleID))
begin
	print('@consoleID must exist in the console table')
	return 1
end
if(not exists(select * from list where ID = @listID))
begin
	print('@list must be an existing table')
	return 2
end
if(exists(select * from ConsoleAppearsOn where ListID = @listID and ConsoleID = @consoleID))
begin
	print('That console is already on the list!')
	return 3
end
insert into ConsoleAppearsOn(ConsoleID, ListID)
values(@consoleID,@listID)
return 0
GO
/****** Object:  StoredProcedure [dbo].[AddGameToList]    Script Date: 2/19/2021 1:12:55 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE procedure [dbo].[AddGameToList]
	@listID int,@gameID int
as
if(not exists(select * from list where ID = @listID))
begin
	print('@listID must be an existing list')
	return 1
end
if(not exists(select * from Game where ID = @gameID))
begin
	print('@gameId must exist in the Games Table')
	return 2
end
if(exists(select * from GameAppearsOn where ListID = @listID and GameID = @gameID))
begin
	print('That game is already on the list!')
	return 3
end
insert into GameAppearsOn(ListID,GameID)
values(@ListID,@gameID)
return 0
GO
/****** Object:  StoredProcedure [dbo].[AddOwnedGame]    Script Date: 2/19/2021 1:12:55 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE procedure [dbo].[AddOwnedGame]
	@gameID int, @user varchar(15)
as
if(not exists(select * from Game where ID = @gameID))
begin
	print('@gameId must exist in the Games Table')
	return 1
end
if(not exists(select * from [User] where Username = @user))
begin
	print('@user must be an existing user')
	return 2
end
if(exists(select * from OwnsGame where OwningUser = @user and @gameID = GameID))
begin
	print('You already own that game!')
	return 3
end
insert into OwnsGame(GameID,OwningUser)
values(@gameID,@user)
GO
/****** Object:  StoredProcedure [dbo].[CreateConsole]    Script Date: 2/19/2021 1:12:55 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE procedure [dbo].[CreateConsole] 
	@consoleName varchar(20)
as
if(@consoleName is null)
begin
	print('@consoleName cannot be null')
	return 1
end
if(exists (select * from Console where Name = @consoleName))
begin
	print('That console already exists')
	return 2
end
insert into Console(Name)
values(@consoleName)
GO
/****** Object:  StoredProcedure [dbo].[CreateFriendship]    Script Date: 2/19/2021 1:12:55 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE procedure [dbo].[CreateFriendship] 
	@frienderUsername varchar(15),
	@friendeeUsername varchar(15)
as
if(@friendeeUsername is null)
begin
	print('@frienderUsername cannot be null')
	return 1
end
if(not exists(select * from [User] where Username = @friendeeUsername))
begin
	print('@friendeeUsername must be an existing user')
	return 2
end
if(exists (select * from FriendsWith where Friendee = @friendeeUsername and Friender = @frienderUsername))
begin
	print('You are already friends with that user')
	return 3
end
insert into FriendsWith(Friendee, Friender)
values (@friendeeUsername, @frienderUsername)
insert into FriendsWith(Friendee, Friender)
values (@frienderUsername,@friendeeUsername)
GO
/****** Object:  StoredProcedure [dbo].[CreateGame]    Script Date: 2/19/2021 1:12:55 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE procedure [dbo].[CreateGame] 
	@gameName varchar(50), 
	@gameDescription varchar(500), 
	@gameGenre varchar(30),
	@gameReleaseDate date,
	@gamePublisher int
as
if(@gameName = '')
begin
	print('@gameName cannot be null or empty')
	return 1
end
if(@gamePublisher is not null and
	@gamePublisher not in (select ID from Publisher where ID = @gamePublisher))
begin
	print('@gamePublisher has to be null or is an existing Publisher')
	return 2
end
if(exists (select * from Game where Name = @gameName))
begin
	print('A game with that name already exists')
	return 3
end
insert into Game([Name], [Description], Genre, ReleaseDate, PublisherID)
Values(@gameName,@gameDescription,@gameGenre,@gameReleaseDate,@gamePublisher)
return 0
GO
/****** Object:  StoredProcedure [dbo].[CreateList]    Script Date: 2/19/2021 1:12:55 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE procedure [dbo].[CreateList]
	@listName varchar(300), @user varchar(15)
as
if(not exists(select * from [User] where Username = @user))
begin
	print('@user must be an existing user')
	return 1
end
if(@listName is null)
begin
	print('@listName cannot be null');
	return 2
end
if(exists (select * from list where Name = @listName))
begin
	print('A list with that name already exists')
	return 3
end
insert into list([Name],OwnerUsername)
values(@listName,@user)
return 0
GO
/****** Object:  StoredProcedure [dbo].[CreatePlayedOn]    Script Date: 2/19/2021 1:12:55 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE procedure [dbo].[CreatePlayedOn]
	@gameID varchar(30),
	@consoleID varchar(30)
as
if(not exists(select * from Game where ID = @gameID))
begin
	print('@gameID must exist in the Game table')
	return 1
end
if(not exists(select * from Console where ID = @consoleID))
begin
	print('@consoleID must exist in the Console table')
	return 2
end
if(exists(select * from PlayedOn where GameID = @gameID and ConsoleID = @consoleID))
begin
	print('PlayedOn connection already exists')
	return 3
end
insert into PlayedOn(GameID,ConsoleID)
values(@gameID,@consoleID)
return 0
GO
/****** Object:  StoredProcedure [dbo].[CreateProfile]    Script Date: 2/19/2021 1:12:55 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE procedure [dbo].[CreateProfile] 
	@profileType varchar(30),
	@profileName varchar(30),
	@OwnerUsername varchar(15)
as
if(@profileName is null)
begin
	print('@profileName cannot be null')
	return 1
end
if(not exists(select * from [User] where Username = @OwnerUsername))
begin
	print('@OwnerUsername must be an existing User')
	return 2
end
if(exists(select * from Profile where Profile.ProfileName = @profileName))
begin
	print('profile already exists')
	return 3
end
insert into Profile(ProfileType, ProfileName, OwnerUsername)
values(@profileType, @profileName, @OwnerUsername)
return 0
GO
/****** Object:  StoredProcedure [dbo].[CreatePublisher]    Script Date: 2/19/2021 1:12:55 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE procedure [dbo].[CreatePublisher] @name varchar(25)
as
if(@name is null)
begin
	print('@name cannot be null')
	return 1
end
insert into Publisher
values(@name)
return 0
GO
/****** Object:  StoredProcedure [dbo].[CreateRating]    Script Date: 2/19/2021 1:12:55 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE procedure [dbo].[CreateRating]
	@gameID int, @user varchar(15), @review varchar(200), @rating float
as
if(not exists(select * from Game where ID = @gameID))
begin
	print('@gameId must exist in the Games Table')
	return 1
end
if(not exists(select * from [User] where Username = @user))
begin
	print('@user must be an existing user')
	return 2
end
if @rating > 5 or @rating < 0
begin
	print('@rating must be inbetween 0 and 5')
	return 3
end
if(exists(select * from Rates where Rates.GameID = @gameID and Rates.RatingUser = @user))
begin
	print('rating already exists')
	return 4
end
insert into rates (GameID,RatingUser,Review,Rating)
values(@gameID,@user,@review,@rating)
GO
/****** Object:  StoredProcedure [dbo].[DeleteList]    Script Date: 2/19/2021 1:12:55 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE procedure [dbo].[DeleteList] @listID int
as
if(not exists(select * from list where ID = @listID))
begin
	print('Cannot delete a list that doesn''t exist')
	return 1
end
delete from list where ID = @listID
return 0
GO
/****** Object:  StoredProcedure [dbo].[DeleteProfile]    Script Date: 2/19/2021 1:12:55 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE Procedure [dbo].[DeleteProfile]
	@profileName nvarchar(30),
	@profileType nvarchar(30)
as
if(not exists(select * from Profile where ProfileName = @profileName))
begin
	print('Profile must exist in the profile table')
	return 1
end
if(not exists(select * from Profile where ProfileType = @profileType))
begin
	print('Profile must exist in the profile table')
	return 2
end
delete from Profile where ProfileName = @profileName and ProfileType = @profileType
return 0
GO
/****** Object:  StoredProcedure [dbo].[DeleteRating]    Script Date: 2/19/2021 1:12:55 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE procedure [dbo].[DeleteRating]
	@gameID int, @user varchar(15)
as
if(not exists(select * from Rates where RatingUser = @user and GameID = @gameID))
begin
	print('Must select a valid review')
	return 1
end
delete from Rates where GameID = @gameID and RatingUser = @user
return 0
GO
/****** Object:  StoredProcedure [dbo].[EditConsoleOnList]    Script Date: 2/19/2021 1:12:55 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE Procedure [dbo].[EditConsoleOnList]
	@listId int,
	@oldConsoleId int,
	@newConsoleid int
as
if(not exists(select * from ConsoleAppearsOn where ListID = @listId and ConsoleID = @oldConsoleId))
begin
	print('Console must appear on the list to edit')
	return 1
end
if(exists(select * from ConsoleAppearsOn where ListId = @listID and ConsoleID = @newConsoleid))
begin
	print('A Console cannot appear on the same list twice')
	return 2
end
update ConsoleAppearsOn
set ConsoleID = @newConsoleid
where @listId = ListID and ConsoleID = @oldConsoleId
--Update
return 0
GO
/****** Object:  StoredProcedure [dbo].[EditGameOnList]    Script Date: 2/19/2021 1:12:55 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE Procedure [dbo].[EditGameOnList]
	@listId int,
	@oldGameId int,
	@newGameid int
as
if(not exists(select * from GameAppearsOn where ListID = @listId and GameID = @oldGameId))
begin
	print('Game must appear on the list to edit')
	return 1
end
if(exists(select * from GameAppearsOn where ListId = @listID and GameID = @newGameid))
begin
	print('A Game cannot appear on the same list twice')
	return 2
end
update GameAppearsOn
set GameID = @newGameid
where @listId = ListID and GameID = @oldGameId
--Update
return 0
GO
/****** Object:  StoredProcedure [dbo].[EditProfile]    Script Date: 2/19/2021 1:12:55 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE Procedure [dbo].[EditProfile]
	@profileId int,
	@newType varchar(30),
	@newName varchar(30)
as
if(not exists(select * from Profile where ID = @profileId))
begin
	print('Profile must exist in the profile table')
	return 1
end
if(@newType is null)
begin
	set @newType = (select ProfileType from Profile where ID =@profileId)
end
if(@newName is null)
begin
	set @newName = (select ProfileName from Profile where ID =@profileId)
end
if(exists(select * from Profile where Profile.ProfileName = @newName and Profile.ProfileType = @newType))
begin
	print('profile already exists')
	return 2
end
update Profile
set ProfileName = @newName, ProfileType=@newType
where ID = @profileId
return 0
GO
/****** Object:  StoredProcedure [dbo].[RegisterUser]    Script Date: 2/19/2021 1:12:55 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE procedure [dbo].[RegisterUser]
	@username varchar(15),
	@password varchar(15)
AS
if @username is null or @password is null
begin
	print('null values are not allowed in the Login Table')
	return 1
end
insert into [User] (Username, Password)
values(@username,@password)
return 0
GO
/****** Object:  StoredProcedure [dbo].[RegisterUserSecure]    Script Date: 2/19/2021 1:12:55 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE procedure [dbo].[RegisterUserSecure]
	@username varchar(15),
	@salt varchar(50),
	@hash varchar(50)
AS
if @username is null or @salt is null or @hash is null
begin
	print('null values are not allowed in the Login Table')
	return 1
end
insert into [User] (Username, PasswordSalt, PasswordHash)
values(@username, @salt, @hash);
return 0
GO
/****** Object:  StoredProcedure [dbo].[RemoveConsoleFromList]    Script Date: 2/19/2021 1:12:55 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE Procedure [dbo].[RemoveConsoleFromList]
	@listId int,
	@consoleID int
as
if(not exists(select * from ConsoleAppearsOn where ListID = @listId and ConsoleID = @consoleID))
begin
	print('Console must appear on the list to remove')
	return 1
end
delete from ConsoleAppearsOn where ConsoleID = @consoleID and ListID = @listId
return 0
GO
/****** Object:  StoredProcedure [dbo].[RemoveFriend]    Script Date: 2/19/2021 1:12:55 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE procedure [dbo].[RemoveFriend]
	@friendee varchar(15),
	@friender varchar(15)
as
if(not exists(select * from FriendsWith where Friendee = @friendee and Friender = @friender))
begin
	print('Users must be friends with eachother to remove freindship')
	return 1
end
delete from FriendsWith where Friendee = @friendee and Friender = @friender
delete from FriendsWith where Friender = @friendee and Friendee = @friender
return 0
GO
/****** Object:  StoredProcedure [dbo].[RemoveGameFromList]    Script Date: 2/19/2021 1:12:55 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE Procedure [dbo].[RemoveGameFromList]
	@listId int,
	@gameId int
as
if(not exists(select * from GameAppearsOn where ListID = @listId and GameID = @gameId))
begin
	print('Game must appear on the list to remove')
	return 1
end
delete from GameAppearsOn where @listId  = ListID and GameID = @gameId
return 0
GO
/****** Object:  StoredProcedure [dbo].[RemoveOwnedGame]    Script Date: 2/19/2021 1:12:55 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE procedure [dbo].[RemoveOwnedGame]
	@gameID int,
	@owningUser varchar(15)
	
as
if(not exists(select * from OwnsGame where OwningUser = @owningUser and GameID = @gameID))
begin
	print('User must own the game')
	return 1
end
delete OwnsGame
where OwningUser = @owningUser and GameID = @gameID
return 0
GO
/****** Object:  StoredProcedure [dbo].[RemovePlayedOn]    Script Date: 2/19/2021 1:12:55 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE procedure [dbo].[RemovePlayedOn]
	@gameID varchar(30),
	@consoleID varchar(30)
as
if(not exists(select * from Game where ID = @gameID))
begin
	print('@gameID must exist in the Game table')
	return 1
end
if(not exists(select * from Console where ID = @consoleID))
begin
	print('@consoleID must exist in the Console table')
	return 2
end
if(not exists(select * from PlayedOn where GameID = @gameID and @consoleID = ConsoleID))
begin
	print('Game must be on a console to remove it from played on')
	return 3
end
delete from PlayedOn where GameID = @gameID and @consoleID = ConsoleID
return 0
GO
/****** Object:  StoredProcedure [dbo].[RemoveUser]    Script Date: 2/19/2021 1:12:55 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE procedure [dbo].[RemoveUser] @username varchar(15)
as
if(@username is null)
begin
	print('Cannot delete Null values')
	return 1
end
if(not exists(select * from [User] where Username = @username))
begin
	print('Cannot find @username in the User table')
	return 2
end
Delete from [User] where Username = @username
return 0
GO
/****** Object:  StoredProcedure [dbo].[UpdateConsole]    Script Date: 2/19/2021 1:12:55 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE Procedure [dbo].[UpdateConsole](@consoleID int, 
							@newName varchar(50))
as
if(@consoleID is null) begin
	print('ID cannot be null')
	return 1
end
if(@newName = null)
begin
	set @newName = (select Name from Console where ID = @consoleID)
end
if(exists(select * from Console where Console.Name = @newName))
begin
	print('console already exists')
	return 2
end
update Console
set Name = @newName
where ID = @consoleID
return 0
GO
/****** Object:  StoredProcedure [dbo].[UpdateGame]    Script Date: 2/19/2021 1:12:55 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE Procedure [dbo].[UpdateGame](@gameID int, 
							@newName varchar(50),
							@newDesc varchar(500), 
							@newGenre varchar(30), 
							@newRelease date, 
							@newPub int)
as
if(@gameID is null) begin
	print('ID cannot be null')
	return 1
end
if(@newName = null)
begin
	set @newName = (select Name from Game where ID = @gameID)
end
if(@newDesc = null)
begin
	set @newDesc = (select Description from Game where ID = @gameID)
end
if(@newGenre = null)
begin
	set @newGenre = (select Genre from Game where ID = @gameID)
end
if(@newRelease = null)
begin
	set @newRelease = (select ReleaseDate from Game where ID = @gameID)
end
if(@newPub = null)
begin
	set @newPub = (select PublisherID from Game where ID = @gameID)
end
if(exists(select * from Game where Game.Name = @newName))
begin
	print('game already exists')
	return 2
end
update Game
set Name = @newName, Description = @newDesc, Genre = @newGenre, ReleaseDate = @newRelease, PublisherID = @newPub
where ID = @gameID
GO
/****** Object:  StoredProcedure [dbo].[UpdatePublisher]    Script Date: 2/19/2021 1:12:55 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE procedure [dbo].[UpdatePublisher]
	@publisherID int,
	@publisherName varchar(25)
as
if(not exists(select * from Publisher where ID = @publisherID))
begin
	print('given publisher must be an existing publisher')
	return 1
end
if(@publisherName is null)
begin
	print('PublisherName cannot be null')
	return 2
end
update Publisher
set Name = @publisherName
where ID = @publisherID
return 0
GO
/****** Object:  StoredProcedure [dbo].[UpdateRating]    Script Date: 2/19/2021 1:12:55 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE procedure [dbo].[UpdateRating]
	@gameID int, @user varchar(15), @review varchar(200) = null, @rating float = null
as
if(not exists(select * from Rates where RatingUser = @user and GameID = @gameID))
begin
	print('Must select a valid review')
	return 1
end
if @rating = -1
begin
	set @rating = (select Rating from  Rates where RatingUser = @user and GameID = @gameID)
end
if @rating > 5 or @rating < 0
begin
	print('@rating must be inbetween 0 and 5')
	return 2
end
if @review is null
begin
	set @review = (select Review from  Rates where RatingUser = @user and GameID = @gameID)
end

update Rates 
set Review = @review, Rating = @rating
where GameID = @gameID and RatingUser = @user
return 0
GO
USE [master]
GO
ALTER DATABASE [GamerBase-S1G8-DEMO] SET  READ_WRITE 
GO



